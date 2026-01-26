package com.example.test.domain.exchange.service;

import com.example.test.domain.exchange.dto.ExchangeRateApiDto;
import com.example.test.domain.exchange.dto.UpbitTickerDto;
import com.example.test.domain.exchange.entity.ExchangeRate;
import com.example.test.domain.exchange.repository.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExchangeRateScheduler {
    private final ExchangeRateRepository exchangeRateRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${api.forex.key}")
    private String forexApiKey;

    // 1분마다 실행
    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void pollExchangeRates() {
        try {
            // 1. 업비트에서 USDT/KRW 현재가 가져오기
            String upbitUrl = "https://api.upbit.com/v1/ticker?markets=KRW-USDT";
            UpbitTickerDto[] upbitRes = restTemplate.getForObject(upbitUrl, UpbitTickerDto[].class);
            BigDecimal usdtKrw = (upbitRes != null && upbitRes.length > 0)
                    ? upbitRes[0].tradePrice() : BigDecimal.ZERO;

            // 2. 글로벌 환율 API에서 USD 기반 환율들 가져오기
            String forexUrl = "https://v6.exchangerate-api.com/v6/" + forexApiKey + "/latest/USD";
            ExchangeRateApiDto forexRes = restTemplate.getForObject(forexUrl, ExchangeRateApiDto.class);

            if (forexRes != null && "success".equals(forexRes.result())) {
                Map<String, BigDecimal> rates = forexRes.rates();

                // DB 1행 업데이트 (이미지의 통화들 위주)
                updateOrSave("KRW", usdtKrw);
                updateOrSave("USD", BigDecimal.ONE);
                updateOrSave("JPY", rates.get("JPY"));
                updateOrSave("CNY", rates.get("CNY"));
                updateOrSave("EUR", rates.get("EUR"));
                updateOrSave("VND", rates.get("VND"));
                updateOrSave("GBP", rates.get("GBP"));

                log.info("환율 동기화 완료: KRW={}, JPY={}", usdtKrw, rates.get("JPY"));
            }
        } catch (Exception e) {
            log.error("환율 폴링 중 오류 발생: {}", e.getMessage());
        }
    }

    private void updateOrSave(String currency, BigDecimal rate) {
        if (rate == null) return;
        ExchangeRate exchangeRate = exchangeRateRepository.findById(currency)
                .orElse(new ExchangeRate(currency, rate));
        exchangeRate.update(rate);
        exchangeRateRepository.save(exchangeRate);
    }


}
