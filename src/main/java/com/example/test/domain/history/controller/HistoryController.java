package com.example.test.domain.history.controller;

import com.example.test.domain.history.controller.impl.HistoryImpl;
import com.example.test.domain.history.dto.request.RequestTransferDto;
import com.example.test.domain.history.dto.response.ResponseHistoryDto;
import com.example.test.domain.history.dto.response.ResponseTransferDto;
import com.example.test.domain.history.enums.Type;
import com.example.test.domain.history.service.HistoryService;
import com.example.test.global.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/history")
public class HistoryController implements HistoryImpl {

    private final HistoryService historyService;

    // 내부 거래 송금 요청
    @Override
    @PostMapping("/transfer")
    public ResponseEntity<ResponseTransferDto> transfer(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody
            RequestTransferDto requestTransferDto
    ) {

        return ResponseEntity.status(HttpStatus.CREATED).body(historyService.requestTransfer(customUserDetails.getId(), requestTransferDto));
    }

    // 내부 거래 내역 조회
    @Override
    @GetMapping
    public ResponseEntity<Page<ResponseHistoryDto>> findHistory(
             @AuthenticationPrincipal CustomUserDetails customUserDetails,
             @RequestParam(required = false) Integer year,
             @RequestParam(required = false) Integer month,
             @RequestParam(required = false, defaultValue = "ALL") Type type,
             @ParameterObject Pageable pageable
    ) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(historyService.findHistory(customUserDetails.getId(), year, month, type, pageable));
    }
}
