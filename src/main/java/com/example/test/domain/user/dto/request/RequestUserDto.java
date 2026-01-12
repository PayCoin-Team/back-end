package com.example.test.domain.user.dto.request;

import com.example.test.domain.user.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


public record RequestUserDto(
        @NotBlank(message = "아이디 채워라")
        @Email
        String username,

        @NotBlank(message = "비밀번호 채워라")
        String password, // 나중에 비밀번호 최소 길이, 특수문자 포함 구현
        String checkPassword,

        @NotBlank(message = "닉네임 채워라")
        String nickName
) {
    public User dtoToEntity(String encodedPassword) {
        return User.builder()
                .username(username)
                .password(encodedPassword)
                .nickName(nickName)
                .build();
    }
}
