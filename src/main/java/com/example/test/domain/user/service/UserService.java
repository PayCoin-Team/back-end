package com.example.test.domain.user.service;

import com.example.test.domain.user.dto.request.ChangePasswordDto;
import com.example.test.domain.user.dto.request.RequestUserDto;
import com.example.test.domain.user.dto.request.UpdateUserDto;
import com.example.test.domain.user.dto.response.ResponseUserDto;
import com.example.test.domain.user.entity.User;
import com.example.test.domain.user.repository.UserRepository;
import com.example.test.global.exception.CustomException;
import com.example.test.global.exception.error.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;

    // 회원 정보 조회
    public ResponseUserDto findUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return ResponseUserDto.dtoToEntity(user);
    }

    // 회원 정보 변경
    public void updateUser(Long userId, UpdateUserDto updateUserDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.updateProfile(updateUserDto);
    }

    // 회원 탈퇴
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.deleteProfile();
    }

    // 회원 비밀번호 변경 (로그인 시)
    public String changePassword(Long userId, ChangePasswordDto dto) {

        // 회원 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 현재 비밀번호 일치 여부
        if(!passwordEncoder.matches(dto.password(), user.getPassword())) {
            throw new CustomException(ErrorCode.CURRENT_PASSWORD_NOT_MATCH);
        }

        // 아이디, 비밀번호 일치 여부 확인
        if(authService.checkPasswordValidation(user.getUsername(), dto.password())) {
            throw new CustomException(ErrorCode.PASSWORD_CONTAIN_USERNAME);
        }

        // 새 비밀번호 일치 여부
        if(!dto.newPassword().equals(dto.checkPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCH);
        }

        String encodePassword = passwordEncoder.encode(dto.newPassword());
        user.setPassword(encodePassword);
        userRepository.save(user);

        return "비밀번호가 변경되었습니다.";
    }
}
