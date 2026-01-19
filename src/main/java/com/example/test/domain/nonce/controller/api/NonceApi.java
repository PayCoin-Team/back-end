package com.example.test.domain.nonce.controller.api;

import com.example.test.domain.nonce.dto.request.NonceRequestDto;
import com.example.test.domain.nonce.dto.request.VerifyRequestDto;
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

@Tag(name = "Wallet 연동 API", description = "지갑 연동(Nonce 발급 및 서명 검증) 관련 API")
public interface NonceApi {

    @Operation(summary = "Nonce 발급 요청", description = "지갑 주소를 전달받아 검증용 랜덤 난수(Nonce)를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nonce 발급 성공",
                    content = @Content(schema = @Schema(implementation = NonceResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content)
    })
    ResponseEntity<NonceResponseDto> getNonce(
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails userDetails,

            @Parameter(description = "연동할 지갑 주소 정보", required = true)
            @RequestBody NonceRequestDto requestDto
    );

    @Operation(summary = "지갑 서명 검증", description = "트론링크에서 서명된 결과물을 검증하여 외부 지갑 연동을 완료합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "지갑 연동 성공",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 서명 또는 만료된 Nonce", content = @Content),
            @ApiResponse(responseCode = "409", description = "이미 등록된 지갑 주소", content = @Content)
    })
    ResponseEntity<String> verify(
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails userDetails,

            @Parameter(description = "서명 데이터 및 검증 정보", required = true)
            @RequestBody VerifyRequestDto request
    );
}