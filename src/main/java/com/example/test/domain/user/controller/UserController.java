package com.example.test.domain.user.controller;

import com.example.test.domain.user.dto.request.RequestUserDto;
import com.example.test.domain.user.dto.request.UpdateUserDto;
import com.example.test.domain.user.dto.response.ResponseUserDto;
import com.example.test.domain.user.service.UserService;
import com.example.test.global.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    // 내 정보 조회
    @GetMapping("/me")
    public ResponseEntity<ResponseUserDto> findUser(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findUser(user.getId()));
    }

    // 내 정보 수정
    @PatchMapping("/update")
    public ResponseEntity<String> updateUser(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody UpdateUserDto updateUserDto
    ) {
        userService.updateUser(user.getId(), updateUserDto);
        return ResponseEntity.status(HttpStatus.OK).body("회원 정보 수정 완료");
    }

    // 회원 탈퇴
    @DeleteMapping("/delete")
    public ResponseEntity<String> withdraw(
        @AuthenticationPrincipal CustomUserDetails user
    ) {
        userService.deleteUser(user.getId());
        return ResponseEntity.status(HttpStatus.OK).body("회원 탈퇴되었습니다.");
    }
}
