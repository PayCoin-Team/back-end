package com.example.test.domain.exchange.service;

import com.example.test.domain.exchange.dto.response.ChartDataDto;
import com.example.test.domain.exchange.entity.ExchangeRate;
import com.example.test.domain.exchange.repository.ExchangeRateRepository;
import com.example.test.global.exception.CustomException;
import com.example.test.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExchangeRateService {
    private final ExchangeRateRepository exchangeRateRepository;

    private final WebClient webClient = WebClient.builder().build();

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

    public List<ChartDataDto> getUsdtChartData(String period) {
        String url;
        switch (period.toUpperCase()) {
            case "1D" -> url = "https://api.upbit.com/v1/candles/minutes/60?market=KRW-USDT&count=24"; // 1시간봉 24개
            case "1W" -> url = "https://api.upbit.com/v1/candles/days?market=KRW-USDT&count=7";      // 일봉 7개
            case "1M" -> url = "https://api.upbit.com/v1/candles/days?market=KRW-USDT&count=30";     // 일봉 30개
            case "1Y" -> url = "https://api.upbit.com/v1/candles/weeks?market=KRW-USDT&count=52";    // 주봉 52개
            default -> throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }

        List<Map<String, Object>> response = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {})
                .block();

        return response.stream()
                .map(candle -> new ChartDataDto(
                        candle.get("candle_date_time_kst").toString(),
                        Double.parseDouble(candle.get("trade_price").toString())
                ))
                .sorted(Comparator.comparing(ChartDataDto::time)) // 시간순 정렬
                .toList();
    }
}
