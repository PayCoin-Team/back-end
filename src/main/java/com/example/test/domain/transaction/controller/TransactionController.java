package com.example.test.domain.transaction.controller;

import com.example.test.domain.transaction.controller.api.TransactionApi;
import com.example.test.domain.transaction.dto.request.RequestWithdrawDto;
import com.example.test.domain.transaction.dto.response.ResponseTransactionDto;
import com.example.test.domain.transaction.service.TransactionService;
import com.example.test.global.security.CustomUserDetails;
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
@RequestMapping("/transaction")
public class TransactionController implements TransactionApi {

    private final TransactionService transactionService;

    @Override
    @PostMapping("/withdraw")
    public ResponseEntity<ResponseTransactionDto> withdraw(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody RequestWithdrawDto requestWithdrawDto
            ) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transactionService.withdraw(user.getId(), requestWithdrawDto));
    }
}
