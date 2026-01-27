package com.example.test.domain.admin.dto.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ResponseHistoryCounts(
        Long todayTransferCounts,
        BigDecimal todayTransferAmount
) {

    public static ResponseHistoryCounts from(Long todayTransferCounts, BigDecimal todayTransferAmount) {
        return ResponseHistoryCounts.builder()
                .todayTransferCounts(todayTransferCounts)
                .todayTransferAmount(todayTransferAmount)
                .build();
    }
}
