package com.example.test.domain.exchange.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 글로벌 환율 API 응답을 위한 Record
 */
public record ExchangeRateApiDto(
        String result,

        @JsonProperty("conversion_rates")
        Map<String, BigDecimal> rates
) {
}
