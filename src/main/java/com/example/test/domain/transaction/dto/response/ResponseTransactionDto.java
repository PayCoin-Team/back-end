package com.example.test.domain.transaction.dto.response;

import com.example.test.domain.transaction.entity.Transaction;
import com.example.test.domain.transaction.enums.Status;
import com.example.test.domain.transaction.enums.Type;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ResponseTransactionDto(
        Long transactionId,
        String txId,
        BigDecimal amount,
        Type type,   // "DEPOSIT", "WITHDRAW"
        Status status, // "PENDING", "SUCCESS"

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt
) {

    public static ResponseTransactionDto from(Transaction transaction) {

        return new ResponseTransactionDto(
                transaction.getId(),
                transaction.getTxid(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getStatus(),
                transaction.getCreatedAt()
        );
    }
}
