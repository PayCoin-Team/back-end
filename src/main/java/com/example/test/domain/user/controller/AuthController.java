package com.example.test.domain.user.controller;

import com.example.test.domain.user.controller.api.AuthApi;
import com.example.test.domain.user.dto.request.RequestUserDto;
import com.example.test.domain.user.service.AuthService;
import com.example.test.global.security.LoginFilter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController implements AuthApi {

    private final AuthService authService;

    @PostMapping("/login")
    public void login(@RequestBody LoginFilter.LoginDto loginDto) {}

    @PostMapping("/logout")
    public void logout() {}

    @Override
    @PostMapping("/signup/send-code")
    public ResponseEntity<String> sendSignupCode(@RequestParam String email) {
        authService.sendSignupCode(email);
        return ResponseEntity.ok("인증번호가 발송되었습니다.");
    }

    @PostMapping("/join")
    public ResponseEntity<String> join(
            @Valid @RequestBody RequestUserDto requestUserDto
    ){
        authService.saveUser(requestUserDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공");
    }

    // 아이디 중복 확인
    @GetMapping("/check-username")
    public ResponseEntity<Boolean> checkUsername(@RequestParam String username) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.checkUsername(username));
    }

    @Override
    @PostMapping("/find-id")
    public ResponseEntity<String> findId(@RequestParam String email) {
        authService.findUsername(email);
        return ResponseEntity.ok("아이디를 메일로 발송했습니다.");
    }

    @Override
    @PostMapping("/password/send-code")
    public ResponseEntity<String> sendCode(@RequestParam String email) {
        authService.sendResetCode(email);
        return ResponseEntity.ok("인증번호가 발송되었습니다.");
    }

    @Override
    @PostMapping("/password/verify-code")
    public ResponseEntity<String> verifyCode(@RequestParam String email, @RequestParam String code) {
        authService.confirmCode(email, code);
        return ResponseEntity.ok("인증 성공");
    }

    @Override
    @PatchMapping("/password/reset")
    public ResponseEntity<String> resetPassword(@RequestParam String email, @RequestParam String newPassword) {
        authService.updatePassword(email, newPassword);
        return ResponseEntity.ok("비밀번호 변경 완료");
    }
}
