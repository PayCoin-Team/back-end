package com.example.test.domain.user.controller;

import com.example.test.domain.user.dto.request.RequestUserDto;
import com.example.test.domain.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/join")
    public ResponseEntity<String> join(
            @Valid @RequestBody
            RequestUserDto requestUserDto
    ){
        authService.saveUser(requestUserDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공");
    }

    // 아이디 중복 확인
    @GetMapping("/check-username")
    public ResponseEntity<Boolean> checkUsername(@RequestParam String username) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.checkUsername(username));
    }
}
