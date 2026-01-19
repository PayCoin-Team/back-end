package com.example.test.domain.user.dto.request;

import com.example.test.domain.user.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


public record RequestUserDto(
        @NotBlank(message = "아이디 채워라")
        String username,

        @NotBlank(message = "채워라")
        @Email // 이메일 형식만 받음
        String email,

        @NotBlank(message = "비밀번호 채워라")
        String password, // 나중에 비밀번호 최소 길이, 특수문자 포함 구현
        String checkPassword,

        @NotBlank(message = "이름 채워라")
        String firstName,

        @NotBlank(message = "이름 채워라")
        String lastName
) {
    public User dtoToEntity(String encodedPassword) {
        return User.builder()
                .username(username)
                .email(email)
                .password(encodedPassword)
                .firstName(firstName)
                .lastName(lastName)
                .build();
    }
}
