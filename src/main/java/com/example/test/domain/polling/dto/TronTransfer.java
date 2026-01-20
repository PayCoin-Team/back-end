package com.example.test.domain.polling.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TronTransfer(
        @JsonProperty("transaction_id") String transactionId,
        @JsonProperty("block_timestamp") long blockTimestamp,
        String from,
        String to,
        String value, // 최소단위 문자열 (USDT면 6 decimals)
        @JsonProperty("token_info") TokenInfo tokenInfo
) {}
