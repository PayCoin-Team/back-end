package com.example.test.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ChangePasswordDto(
        @NotBlank
        String password,

        @NotBlank
        String newPassword,

        @NotBlank
        String checkPassword
) {
}
