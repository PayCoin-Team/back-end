package com.example.test.domain.userwallet.controller;

import com.example.test.domain.userwallet.dto.response.ResponseUserWalletDto;
import com.example.test.domain.userwallet.controller.api.UserWalletApi;
import com.example.test.domain.userwallet.service.UserWalletService;
import com.example.test.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserWalletController implements UserWalletApi {

    private final UserWalletService userWalletService;

    // 회원 내부 지갑 정보 조회
    @Override
    @GetMapping("/wallets/users/me")
    public ResponseEntity<ResponseUserWalletDto> findUserWallet(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {

        return ResponseEntity.status(HttpStatus.OK).body(userWalletService.findUserWallet(customUserDetails.getId()));
    }
}
