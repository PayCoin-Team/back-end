package com.example.test.domain.userwallet.controller.impl;

import com.example.test.domain.history.dto.response.ResponseUserWalletDto;
import com.example.test.domain.nonce.dto.response.NonceResponseDto;
import com.example.test.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Tag(name = "UserWallet API", description = "사용자 내부 지갑 관련 API")
public interface UserWalletImpl {

    @Operation(summary = "사용자 내부 지갑 정보 조회", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = NonceResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content),
            @ApiResponse(responseCode = "404", description = "해당 지갑 조회 실패", content = @Content)
    })
    ResponseEntity<ResponseUserWalletDto> findUserWallet(
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails customUserDetails
            );
}
