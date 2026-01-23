package com.example.test.domain.transaction.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

// txid 단건 조회 응답용, 중첩 record 형태
@JsonIgnoreProperties(ignoreUnknown = true)
public record TronTxidResponse(Long blockNumber,
                               Receipt receipt,
                               List<LogEntry> log) {

    public record Receipt(String result) {}

    public record LogEntry(
            String address,      // 컨트랙트 주소
            List<String> topics, // 토픽 목록(0=Transfer 해시, 1=from_address, 2=to_address)
            String data          // amount, 16진수 형태
    ) {}
}
