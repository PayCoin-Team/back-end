package com.example.test.domain.user.dto.request;

import com.example.test.domain.user.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;


public record RequestUserDto(
        @NotBlank(message = "아이디 채워라")
        String username,

        @NotBlank(message = "채워라")
        @Email // 이메일 형식만 받음
        String email,

        @NotBlank(message = "비밀번호 채워라")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$",
                message = "비밀번호는 영문, 숫자, 특수문자를 포함하여 8~20자리여야 합니다.")
        String password,
        String checkPassword,

        @NotBlank(message = "이름 채워라")
        String firstName,

        @NotBlank(message = "이름 채워라")
        String lastName,

        String code
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
