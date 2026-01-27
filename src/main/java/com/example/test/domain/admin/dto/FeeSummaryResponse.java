package com.example.test.domain.admin.dto;

import lombok.Builder;

import java.math.BigDecimal;


public record FeeSummaryResponse(BigDecimal totalFees,
                                 BigDecimal yesterdayFees,
                                 BigDecimal serverBalance,
                                 BigDecimal userBalance) {
}
