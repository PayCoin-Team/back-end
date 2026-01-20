package com.example.test.domain.history.controller;

import com.example.test.domain.history.controller.impl.HistoryImpl;
import com.example.test.domain.history.dto.request.RequestTransferDto;
import com.example.test.domain.history.dto.response.ResponseTransferDto;
import com.example.test.domain.history.service.HistoryService;
import com.example.test.global.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/history")
public class HistoryController implements HistoryImpl {

    private final HistoryService historyService;

    @Override
    @PostMapping("/transfer")
    public ResponseEntity<ResponseTransferDto> transfer(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody
            RequestTransferDto requestTransferDto
    ) {

        return ResponseEntity.status(HttpStatus.CREATED).body(historyService.requestTransfer(customUserDetails.getId(), requestTransferDto));
    }
}
