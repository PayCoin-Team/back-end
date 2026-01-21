package com.example.test.domain.user.controller.impl;

import com.example.test.domain.nonce.dto.response.NonceResponseDto;
import com.example.test.domain.user.dto.response.ResponseUserDto;
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

@Tag(name = "회원 관련 API", description = "회원 관련(조회, 수정, 탈퇴) 기능 API")
public interface UserImpl {

    @Operation(summary = "Nonce 발급 요청", description = "지갑 주소를 전달받아 검증용 랜덤 난수(Nonce)를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 조회 성공",
                    content = @Content(schema = @Schema(implementation = NonceResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content)
    })
    ResponseEntity<ResponseUserDto> findUser(
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails user
    );
}
