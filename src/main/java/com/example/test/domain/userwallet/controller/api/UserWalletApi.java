package com.example.test.domain.userwallet.controller.api;

import com.example.test.domain.userwallet.dto.response.ResponseUserWalletDto;
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
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "UserWallet API", description = "사용자 내부 지갑 관련 API")
public interface UserWalletApi {

    @Operation(summary = "사용자 내부 지갑 정보 조회", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = ResponseUserWalletDto.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content),
            @ApiResponse(responseCode = "404", description = "해당 지갑 조회 실패", content = @Content)
    })
    ResponseEntity<ResponseUserWalletDto> findUserWallet(
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails customUserDetails
            );

    @Operation(summary = "입금 대상 주소 확인", description = "공개 주소를 입력받아 해당 사용자가 존재하는지 확인하고 전체 이름을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유효한 주소 (사용자 이름 반환)"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 주소", content = @Content)
    })
    ResponseEntity<String> verifyAddress(
            @Parameter(description = "대상자의 public 지갑 주소", example = "A1B2-C3D4")
            @RequestParam("address") String address
    );
}
