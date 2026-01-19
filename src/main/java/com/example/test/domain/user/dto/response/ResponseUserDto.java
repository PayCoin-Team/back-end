package com.example.test.domain.user.dto.response;

import com.example.test.domain.user.entity.User;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ResponseUserDto(
        String username,
        String email,
        String firstName,
        String lastName,
        BigDecimal balance,
        String publicAddress
) {
    public static ResponseUserDto dtoToEntity(User user) {
        return ResponseUserDto.builder()
                .username((user.getUsername()))
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .balance(user.getUserWallet().getBalance())
                .publicAddress(user.getUserWallet().getPublicAddress())
                .build();
    }
}
