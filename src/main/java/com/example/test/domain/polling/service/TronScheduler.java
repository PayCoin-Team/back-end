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
        pollingService.pollingUSDT();
    }

}
