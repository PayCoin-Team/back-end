package com.example.test.domain.polling.service;

import com.example.test.domain.polling.config.TronProperties;
import com.example.test.domain.polling.dto.TronGridResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
@Service
@Slf4j
public class TronScheduler {
    private final WebClient tronGridClient;
    private final TronProperties properties;
    private long lastTimestamp = System.currentTimeMillis();
    private final PollingService pollingService;

    @Scheduled(fixedDelayString = "${tron.polling.interval-ms}")
    public void pollingUsdt(){

        tronGridClient.get().uri(uriBuilder -> uriBuilder.path("/v1/contracts/{address}/events") // polling api 경로
                .queryParam("event_name", "Transfer")
                .queryParam("order_by", "timestamp,asc")
                .build(properties.token().usdtContract())) // {address}에 컨트랙트 주소 넣기
                .retrieve() // 응답 추출
                .bodyToMono(TronGridResponse.class) // Body를 자바 객체로
                .subscribe(response -> {
                    if (response != null && response.success()) {
                        pollingService.processEvents(response.data());
                    }
                });
    }

}
