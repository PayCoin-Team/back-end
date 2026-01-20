package com.example.test.domain.polling.service;

import com.example.test.domain.polling.config.TronProperties;
import com.example.test.domain.polling.dto.TronGridResponse;
import com.example.test.domain.polling.model.Polling;
import com.example.test.domain.polling.repository.PollingRepository;
import com.example.test.global.exception.CustomException;
import com.example.test.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
@Service
@Slf4j
public class TronScheduler {
    private final WebClient tronGridClient;
    private final TronProperties properties;
    private final PollingService pollingService;
    private final PollingRepository pollingRepository;

    @Scheduled(fixedDelayString = "${tron.polling.interval-ms}")
    @Transactional
    public void pollingUsdt(){

        Polling polling = pollingRepository.findById(1L).orElseThrow(() -> new CustomException(ErrorCode.ROW_NOT_FOUND));

        long startBlock = polling.getLastBlock();

        tronGridClient.get().uri(uriBuilder -> uriBuilder.path("/v1/contracts/{address}/events") // polling api 경로
                .queryParam("event_name", "Transfer")
                .queryParam("order_by", "timestamp,asc")
                .queryParam("min_block_number", startBlock + 1)
                .build(properties.token().usdtContract())) // {address}에 컨트랙트 주소 넣기
                .retrieve() // 응답 추출
                .bodyToMono(TronGridResponse.class) // 응답Body를 자바 객체로
                .subscribe(pollingService::applyLatestBlockAndProcessEvents);
    }

}
