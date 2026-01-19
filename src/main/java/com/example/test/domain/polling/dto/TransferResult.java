package com.example.test.domain.polling.dto;

// 컨트랙트 상세 정보
public record TransferResult(
        String from,
        String to,
        String value
) {}
