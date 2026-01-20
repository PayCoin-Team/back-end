package com.example.test.domain.exchange.dto.response;

import com.example.test.domain.exchange.entity.ExchangeRate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ExchangeRateResponseDto(
        String currency,
        BigDecimal rate,
        LocalDateTime updatedAt
) {
    public static ExchangeRateResponseDto from(ExchangeRate entity) {
        return new ExchangeRateResponseDto(
                entity.getTargetCurrency(),
                entity.getRate(),
                entity.getUpdatedAt()
        );
    }
}