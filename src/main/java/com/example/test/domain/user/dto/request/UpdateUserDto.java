package com.example.test.domain.user.dto.request;

public record UpdateUserDto(
        String username,
        String firstName,
        String lastName
) {
}
