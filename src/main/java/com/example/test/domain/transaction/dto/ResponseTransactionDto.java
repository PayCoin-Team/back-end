package com.example.test.domain.transaction.dto;

import com.example.test.domain.transaction.entity.Status;
import com.example.test.domain.transaction.entity.Type;

public record ResponseTransactionDto(Long transactionId,
                                     String txid,
                                     Status status,
                                     Type type) {
}
