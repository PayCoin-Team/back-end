package com.example.test.domain.exchange.service;

import com.example.test.domain.exchange.entity.ExchangeRate;
import com.example.test.domain.exchange.repository.ExchangeRateRepository;
import com.example.test.global.exception.CustomException;
import com.example.test.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExchangeRateService {
    private final ExchangeRateRepository exchangeRateRepository;

    // 모든 통화의 최신 환율 가져오기
    @Transactional(readOnly = true)
    public List<ExchangeRate> getAllRates() {
        return exchangeRateRepository.findAll();
    }

    // 특정 통화의 환율 가져오기
    @Transactional(readOnly = true)
    public BigDecimal getRate(String targetCurrency) {
        return exchangeRateRepository.findById(targetCurrency)
                .map(ExchangeRate::getRate)
                .orElseThrow(() -> new CustomException(ErrorCode.EXCHANGE_RATE_NOT_FOUND));
    }

    // 잔고를 타겟 통화 가치로 변환
    public BigDecimal convert(BigDecimal usdtAmount, String targetCurrency) {
        BigDecimal rate = getRate(targetCurrency);
        return usdtAmount.multiply(rate);
    }
}
