package com.example.test.domain.history.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Type {

    ALL, // 전체
    DEPOSIT, // 송금
    WITHDRAWAL // 출금
}
