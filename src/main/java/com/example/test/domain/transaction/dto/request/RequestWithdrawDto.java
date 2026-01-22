package com.example.test.domain.transaction.dto.request;

import com.example.test.domain.externalWallet.entity.ExternalWallet;
import com.example.test.domain.transaction.entity.Transaction;
import com.example.test.domain.transaction.enums.Type;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record RequestWithdrawDto(
        @NotNull(message = "출금 금액은 필수입니다.")
        @DecimalMin(value = "0.000001", message = "최소 출금 금액을 확인해주세요.") // USDT 최소 단위 고려
        BigDecimal amount,

        @NotBlank(message = "출금할 지갑 주소는 필수입니다.")
        String walletAddress
) {

        public Transaction dtoToEntity(ExternalWallet externalWallet, String serviceAddress) {

                return Transaction.builder()
                        .amount(amount)
                        .toAddress(walletAddress)
                        .fromAddress(serviceAddress)
                        .txId(null)
                        .type(Type.WITHDRAW)
                        .externalWallet(externalWallet)
                        .build();
        }
}
