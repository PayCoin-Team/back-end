package com.example.test.domain.userwallet.dto.response;

import com.example.test.domain.userwallet.entity.UserWallet;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record ResponseUserWalletDto(
        Long userId,
        BigDecimal balance,
        String publicAddress,
        List<String> externalAddress
) {

    public static ResponseUserWalletDto from(UserWallet userWallet, List<String> externalAddress) {

        return ResponseUserWalletDto.builder()
                .userId(userWallet.getId())
                .balance(userWallet.getBalance())
                .publicAddress(userWallet.getPublicAddress())
                .externalAddress(externalAddress)
                .build();
    }
}
