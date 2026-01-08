package com.example.test.domain.user.controller;

import com.example.test.domain.user.dto.request.RequestUserDto;
import com.example.test.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody RequestUserDto requestUserDto){
        userService.saveUser(requestUserDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공");
    }
}
