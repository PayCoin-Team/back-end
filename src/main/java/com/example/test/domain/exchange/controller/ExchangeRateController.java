package com.example.test.domain.exchange.controller;

import com.example.test.domain.exchange.controller.api.ExchangeRateApi;
import com.example.test.domain.exchange.dto.response.ExchangeRateResponseDto;
import com.example.test.domain.exchange.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/exchange")
@RequiredArgsConstructor
public class ExchangeRateController implements ExchangeRateApi {

    private final ExchangeRateService exchangeRateService;

    @Override
    public ResponseEntity<List<ExchangeRateResponseDto>> getAllRates() {
        return ResponseEntity.ok(
                exchangeRateService.getAllRates().stream()
                        .map(ExchangeRateResponseDto::from)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public ResponseEntity<BigDecimal> convert(BigDecimal amount, String target) {
        return ResponseEntity.ok(exchangeRateService.convert(amount, target));
    }
}
