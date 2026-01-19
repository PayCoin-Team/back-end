package com.example.test.domain.nonce.controller;

import com.example.test.domain.nonce.controller.api.NonceApi;
import com.example.test.domain.nonce.dto.request.NonceRequestDto;
import com.example.test.domain.nonce.dto.request.VerifyRequestDto;
import com.example.test.domain.nonce.dto.response.NonceResponseDto;
import com.example.test.domain.nonce.service.WalletVerifyService;
import com.example.test.global.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wallets")
@RequiredArgsConstructor
public class NonceController implements NonceApi {
    private final WalletVerifyService walletVerifyService;

    @Override
    @PostMapping("/nonce")
    public ResponseEntity<NonceResponseDto> getNonce(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody NonceRequestDto requestDto) {
        return  ResponseEntity.ok(walletVerifyService.generateNonce(userDetails.getId(), requestDto));
    }

    @Override
    @PostMapping("/verify")
    public ResponseEntity<String> verify(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody VerifyRequestDto request) {

        walletVerifyService.verifyAndLink(userDetails.getId(), request);
        return ResponseEntity.ok("지갑 연동이 성공적으로 완료되었습니다.");
    }
}
