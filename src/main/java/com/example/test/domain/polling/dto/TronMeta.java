package com.example.test.domain.polling.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TronMeta(
        String fingerprint // 받은 트랜잭션 데이터에 다음 페이지가 있는지
) {}