package com.example.test.domain.history.controller.impl;

import com.example.test.domain.history.dto.request.RequestTransferDto;
import com.example.test.domain.history.dto.response.ResponseTransferDto;
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
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "History API", description = "내부 지갑 거래(내부 송금, 거래 내역 조회) 관련 API")
public interface HistoryImpl {

    @Operation(summary = "내부 송금 요청", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "내부 송금 성공",
                    content = @Content(schema = @Schema(implementation = NonceResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content),
            @ApiResponse(responseCode = "404", description = "해당 지갑 주소 없음", content = @Content)
    })
    ResponseEntity<ResponseTransferDto> transfer(
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails customUserDetails,

            @Parameter(description = "송금 요청 정보 (받는 사람 주소, 보낼 금액)", required = true)
            @RequestBody RequestTransferDto requestTransferDto
    );
}
