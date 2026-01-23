package com.example.test.domain.polling.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TronAccountResponse(boolean success,
                                  List<AccountData> data) {
    // 데이터
    public record AccountData(
            String address,
            long balance, // TRX 잔액 (Sun 단위)
            @JsonProperty("trc20")
            List<Map<String, String>> trc20
    ) {}
}
