package com.example.test.domain.userwallet.dto.response;

import com.example.test.domain.user.enums.Role;
import com.example.test.domain.userwallet.entity.UserWallet;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record ResponseUserWalletDto(
        Long userId,
        BigDecimal balance,
        String publicAddress,
        String externalAddress,
        Role role
) {

    public static ResponseUserWalletDto from(UserWallet userWallet, String externalAddress) {

        return ResponseUserWalletDto.builder()
                .userId(userWallet.getId())
                .balance(userWallet.getBalance())
                .publicAddress(userWallet.getPublicAddress())
                .externalAddress(externalAddress)
                .role(userWallet.getUser().getRole())
                .build();
    }
}
