package com.example.test.domain.polling.controller;

import com.example.test.domain.polling.controller.api.TronAPI;
import com.example.test.domain.polling.dto.ServerBalnceResponse;
import com.example.test.domain.polling.service.PollingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/server_wallet")
@RequiredArgsConstructor
public class TronController implements TronAPI {

    private final PollingService pollingService;

    @Override
    public ResponseEntity<ServerBalnceResponse> getServerBalance() {
        return ResponseEntity.ok(pollingService.getUsdtBalance());
    }
}
