package com.example.test.domain.exchange.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExchangeRate {
    @Id
    private String targetCurrency; // "KRW", "USD" 등

    @Column(nullable = false)
    private BigDecimal rate; // 1 USDT 당 환율 값

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public ExchangeRate(String targetCurrency, BigDecimal rate) {
        this.targetCurrency = targetCurrency;
        this.rate = rate;
        this.updatedAt = LocalDateTime.now();
    }
    public void update(BigDecimal newRate) {
        this.rate = newRate;
        this.updatedAt = LocalDateTime.now();
    }
}
