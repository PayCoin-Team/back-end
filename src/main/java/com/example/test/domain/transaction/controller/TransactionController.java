package com.example.test.domain.transaction.controller;

import com.example.test.domain.transaction.controller.api.TransactionApi;
import com.example.test.domain.transaction.dto.request.RequestTransactionDto;
import com.example.test.domain.transaction.dto.response.ResponseTransactionDto;
import com.example.test.domain.transaction.enums.Type;
import com.example.test.domain.transaction.service.TransactionService;
import com.example.test.global.security.CustomUserDetails;
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
@RequestMapping("/transaction")
public class TransactionController implements TransactionApi {

    private final TransactionService transactionService;

    // 입금 요청
    @Override
    @PostMapping("/deposit")
    public ResponseEntity<ResponseTransactionDto> deposit(
           @AuthenticationPrincipal CustomUserDetails customUserDetails,
           @RequestBody RequestTransactionDto requestTransactionDto
    ) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transactionService.deposit(customUserDetails.getId(), requestTransactionDto));
    }

    // 출금 요청
    @Override
    @PostMapping("/withdraw")
    public ResponseEntity<ResponseTransactionDto> withdraw(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody RequestTransactionDto requestTransactionDto
            ) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transactionService.withdraw(customUserDetails.getId(), requestTransactionDto));
    }

    // 특정 사용자 입/출금 거래 내역 조회
    @Override
    @GetMapping
    public ResponseEntity<Page<ResponseTransactionDto>> findTransaction(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Type type,
            @ParameterObject Pageable pageable
    ) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(transactionService.findTransaction(customUserDetails.getId(), year, month, type, pageable));
    }

    @Override
    @GetMapping("/deposit/{transactionId}/{txid}")
    public ResponseEntity<ResponseTransactionDto> confirmDeposit(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long transactionId,
            @PathVariable String txid) {
        return ResponseEntity.ok(
                transactionService.confirmDeposit(transactionId, txid, customUserDetails.getId()));
    }
}
