package com.example.test.domain.polling.dto;

import java.math.BigDecimal;

public record ServerBalnceResponse(BigDecimal usdtBalance,
                                   BigDecimal trxBalance,
                                   String serverAddress) {
}
