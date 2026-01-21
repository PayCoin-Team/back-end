package com.example.test.domain.history.dto.request;

import com.example.test.domain.history.entity.History;
import com.example.test.domain.userwallet.entity.UserWallet;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record RequestTransferDto(
        @NotBlank
        String targetAddress,

        @NotNull @Positive // 양수만 입력 가능
        BigDecimal amount
) {

    public History dtoToEntity(UserWallet receiver, UserWallet sender) {
        return History.builder()
                .amount(amount)
                .sender(sender)
                .receiver(receiver)
                .build();
    }
}
