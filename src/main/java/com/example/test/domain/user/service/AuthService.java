package com.example.test.domain.user.service;

import com.example.test.domain.user.dto.request.RequestUserDto;
import com.example.test.domain.user.entity.User;
import com.example.test.domain.user.repository.UserRepository;
import com.example.test.domain.userwallet.entity.UserWallet;
import com.example.test.domain.userwallet.service.UserWalletService;
import com.example.test.global.exception.CustomException;
import com.example.test.global.exception.error.ErrorCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserWalletService userWalletService;
    private final JavaMailSender mailSender;
    private final VerificationService verificationService;


    // 회원가입
    public void saveUser(RequestUserDto requestUserDto) {
        // 아이디 중복 예외
        if(userRepository.existsByUsername(requestUserDto.username())) {
            throw new CustomException(ErrorCode.DUPLICATE_USERNAME);
        }

        // 비밀번호 불일치
        if(!requestUserDto.password().equals(requestUserDto.checkPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCH);
        }

        String encodedPassword = passwordEncoder.encode(requestUserDto.password());
        User user = requestUserDto.dtoToEntity(encodedPassword);

        // 유저 내부 지갑 생성
        UserWallet userWallet = userWalletService.generate(user);
        user.setUserWallet(userWallet);

        userRepository.save(user);
    }

    // 공통 이메일 발송 메서드
    private void sendMail(String to, String subject, String content) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(new InternetAddress("oh1334000@gmail.com", "CrossPay", "UTF-8"));
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            mailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    // 아이디 찾기: 이메일로 가입된 유저의 아이디 전송
    public void findUsername(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        String htmlContent = getEmailHtmlLayout(
                "아이디 찾기 결과 안내",
                "고객님의 이메일로 가입된 아이디 정보입니다.",
                user.getUsername(),
                "아이디를 확인하신 후 로그인을 진행해 주세요."
        );

        sendMail(email, "[CrossPay] 아이디 찾기 안내입니다.", htmlContent);
    }

    // 비밀번호 재설정 - 인증번호 발송
    public void sendResetCode(String email) {
        userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        String code = UUID.randomUUID().toString().substring(0, 6);
        verificationService.saveCode(email, code);

        String htmlContent = getEmailHtmlLayout(
                "비밀번호 재설정 인증번호",
                "요청하신 비밀번호 재설정을 위해 아래 인증번호를 입력창에 입력해 주세요.",
                code,
                "인증번호는 보안을 위해 5분 동안만 유효합니다."
        );

        sendMail(email, "[CrossPay] 비밀번호 재설정 인증번호 안내", htmlContent);
    }

    // 비밀번호 재설정 - 인증번호 확인
    public void confirmCode(String email, String code) {
        if (!verificationService.verifyCode(email, code)) {
            throw new CustomException(ErrorCode.INVALID_SIGNATURE);
        }
    }

    // 비밀번호 재설정 - 실제 비밀번호 변경
    public void updatePassword(String email, String newPassword) {
        if (!verificationService.isVerified(email)) {
            throw new CustomException(ErrorCode.INVALID_USER_ATTEMPT);
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.setPassword(passwordEncoder.encode(newPassword));
        verificationService.clear(email);
    }

    // 공통 디자인 레이아웃
    private String getEmailHtmlLayout(String title, String subTitle, String mainValue, String footerText) {
        String brandColor = "#00C897";

        return "<div style='background-color: #f8f9fa; padding: 50px 20px; font-family: sans-serif;'>" +
                "  <div style='max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 16px; overflow: hidden; box-shadow: 0 4px 12px rgba(0,0,0,0.05);'>" +
                "    <div style='background-color: " + brandColor + "; padding: 30px; text-align: center;'>" +
                "      <h1 style='color: #ffffff; margin: 0; font-size: 28px; letter-spacing: -1px; font-weight: bold;'>CrossPay</h1>" +
                "    </div>" +
                "    <div style='padding: 40px 30px; text-align: center;'>" +
                "      <h2 style='font-size: 22px; color: #333; margin-bottom: 10px;'>" + title + "</h2>" +
                "      <p style='color: #666; font-size: 16px; line-height: 1.6; margin-bottom: 30px;'>" + subTitle + "</p>" +
                "      <div style='background-color: #f1f3f5; border-radius: 12px; padding: 25px; margin-bottom: 30px;'>" +
                "        <span style='font-size: 32px; font-weight: 800; color: " + brandColor + "; letter-spacing: 4px;'>" + mainValue + "</span>" +
                "      </div>" +
                "      <p style='font-size: 14px; color: #999;'>" + footerText + "</p>" +
                "    </div>" +
                "    <div style='background-color: #f8f9fa; padding: 20px; text-align: center; border-top: 1px solid #eee;'>" +
                "      <p style='font-size: 12px; color: #adb5bd; margin: 0;'>© 2026 CrossPay. All rights reserved.</p>" +
                "    </div>" +
                "  </div>" +
                "</div>";
    }

    // 아이디 중복 확인
    public boolean checkUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}
