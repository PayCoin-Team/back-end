package com.example.test.domain.user.controller;

import com.example.test.domain.user.dto.request.RequestUserDto;
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

    @PostMapping()
    public ResponseEntity<String> join(
            @Valid
            @RequestBody
            RequestUserDto requestUserDto
    ){
        userService.saveUser(requestUserDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공");
    }


    @GetMapping("/find")
    public ResponseEntity<ResponseUserDto> findUser(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.findUser(user.getId()));
    }
}
