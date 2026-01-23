package com.example.test.domain.admin.dto.response;

import com.example.test.domain.transaction.entity.Transaction;
import com.example.test.domain.transaction.enums.Type;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ResponseAllTransactionDto(
        Long transactionId,
        String externalAddress,
        BigDecimal amount,
        Type type,   // "DEPOSIT", "WITHDRAW"

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt
) {
        public static ResponseAllTransactionDto from(Transaction transaction) {

                String address;
                if(transaction.getType().equals(Type.DEPOSIT)) {
                        address = transaction.getFromAddress();
                } else {
                        address = transaction.getToAddress();
                }

                return new ResponseAllTransactionDto(
                        transaction.getId(),
                        address,
                        transaction.getAmount(),
                        transaction.getType(),
                        transaction.getCreatedAt()
                );
        }
}
