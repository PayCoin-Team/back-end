package com.example.test.domain.history.dto.response;

import com.example.test.domain.user.entity.User;
import com.example.test.domain.userwallet.entity.UserWallet;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ResponseUserWalletDto(
        Long userId,
        BigDecimal balance,
        String publicAddress
) {

    public static ResponseUserWalletDto dtoToEntity(UserWallet userWallet) {

        return ResponseUserWalletDto.builder()
                .userId(userWallet.getId())
                .balance(userWallet.getBalance())
                .publicAddress(userWallet.getPublicAddress())
                .build();
    }
}
