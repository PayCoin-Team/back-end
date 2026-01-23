package com.example.test.domain.user.dto.response;

import com.example.test.domain.user.entity.User;
import com.example.test.domain.user.enums.Role;
import com.example.test.domain.userwallet.entity.UserWallet;

import java.math.BigDecimal;

public record UserStatusResponse(
        Long id,
        String username,
        String firstName,
        String lastName,
        Role role,
        BigDecimal balance,
        String publicAddress

) {

    public static UserStatusResponse from(User user, UserWallet userWallet) {
        return new UserStatusResponse(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole(),
                userWallet.getBalance(),
                userWallet.getPublicAddress()
        );
    }
}
