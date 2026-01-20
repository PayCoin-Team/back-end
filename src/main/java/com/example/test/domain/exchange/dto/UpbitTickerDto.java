package com.example.test.domain.exchange.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

/**
 * 업비트 API 응답을 위한 Record
 */
public record UpbitTickerDto(
        String market,

        @JsonProperty("trade_price")
        BigDecimal tradePrice
) {
}
