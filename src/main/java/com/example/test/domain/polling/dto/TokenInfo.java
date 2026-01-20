package com.example.test.domain.polling.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// 선언된 변수들 말고는 받지 않도록 설정
@JsonIgnoreProperties(ignoreUnknown = true)
public record TokenInfo(String symbol, // 토큰(usdt 확인용)
                        String address, // 컨트랙트 주소
                        Integer decimals // 금액 변환용(decimal이 6이기 때문에 나중에 변환해야함)
) {}

