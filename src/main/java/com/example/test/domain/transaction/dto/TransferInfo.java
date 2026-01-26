package com.example.test.domain.transaction.dto;

import java.math.BigDecimal;

public record TransferInfo(String from, String to, BigDecimal amount) {
}
