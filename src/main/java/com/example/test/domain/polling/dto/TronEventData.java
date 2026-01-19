package com.example.test.domain.polling.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

// 온체인 거래 정보 DTO
public record TronEventData(
        @JsonProperty("transaction_id") String transactionId,
        @JsonProperty("block_number") Long blockNumber,
        @JsonProperty("event_name") String eventName,
        @JsonProperty("result") TransferResult result,
        long timestamp
) {}
