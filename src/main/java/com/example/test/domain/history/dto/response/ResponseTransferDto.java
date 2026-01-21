package com.example.test.domain.history.dto.response;

import java.math.BigDecimal;

public record ResponseTransferDto(
        Long historyId,
        BigDecimal remainBalance
) {
}
