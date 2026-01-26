package com.example.test.domain.admin.dto.response;

import com.example.test.domain.history.entity.History;
import com.example.test.domain.transaction.entity.Transaction;
import com.example.test.domain.transaction.enums.Type;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record UserTransferDto(
        Long id,
        String type,
        BigDecimal amount,
        String counterparty, // 상대방 지갑 주소(거래)
        LocalDateTime time
) {

    public static UserTransferDto from(Transaction tx) {

        String counterparty = tx.getType() == Type.DEPOSIT ? tx.getFromAddress() : tx.getToAddress();

        return UserTransferDto.builder()
                .id(tx.getId())
                .type(tx.getType().toString())
                .amount(tx.getAmount())
                .counterparty(counterparty)
                .time(tx.getCreatedAt())
                .build();
    }

    public static UserTransferDto from(History hi, Long currentUserId) {

        String type = hi.getSender().getId().equals(currentUserId) ? "보내기" : "받기";
        String counterparty = hi.getSender().getId() == currentUserId
                ? hi.getReceiver().getPublicAddress() : hi.getSender().getPublicAddress();

        return UserTransferDto.builder()
                .id(hi.getId())
                .type(type)
                .amount(hi.getAmount())
                .counterparty(counterparty)
                .time(hi.getCreatedAt())
                .build();
    }
}
