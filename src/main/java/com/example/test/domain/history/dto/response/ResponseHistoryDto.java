package com.example.test.domain.history.dto.response;

import com.example.test.domain.history.entity.History;
import com.example.test.domain.history.enums.Type;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ResponseHistoryDto(
        Long historyId,
        BigDecimal amount,
        String senderFirstName,
        String senderLastName,
        String receiverFirstName,
        String receiverLastName,
        String senderAddress,
        String receiverAddress,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime transactionTime,

        Type type

) {

        public static ResponseHistoryDto of(History history, Long viewerId) {
                Type transferType = history.getReceiver().getId() == viewerId
                        ? Type.DEPOSIT
                        : Type.WITHDRAWAL;

                return new ResponseHistoryDto(
                        history.getId(),
                        history.getAmount(),
                        history.getSender().getUser().getFirstName(),
                        history.getSender().getUser().getLastName(),
                        history.getReceiver().getUser().getFirstName(),
                        history.getReceiver().getUser().getLastName(),
                        history.getSender().getPublicAddress(),
                        history.getReceiver().getPublicAddress(),
                        history.getCreatedAt(),
                        transferType
                );
        }
}
