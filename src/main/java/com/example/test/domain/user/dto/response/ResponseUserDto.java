package com.example.test.domain.user.dto.response;

import com.example.test.domain.user.entity.User;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ResponseUserDto(
        String username,
        String email,
        String firstName,
        String lastName
) {
    public static ResponseUserDto dtoToEntity(User user) {
        return ResponseUserDto.builder()
                .username((user.getUsername()))
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }
}
