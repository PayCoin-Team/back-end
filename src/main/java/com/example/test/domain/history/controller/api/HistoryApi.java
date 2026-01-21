package com.example.test.domain.history.controller.api;

import com.example.test.domain.history.dto.request.RequestTransferDto;
import com.example.test.domain.history.dto.response.ResponseHistoryDto;
import com.example.test.domain.history.dto.response.ResponseTransferDto;
import com.example.test.domain.history.enums.Type;
import com.example.test.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "History API", description = "내부 지갑 거래(내부 송금, 거래 내역 조회) 관련 API")
public interface HistoryApi {

    @Operation(summary = "내부 송금 요청", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "내부 송금 성공",
                    content = @Content(schema = @Schema(implementation = ResponseTransferDto.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content),
            @ApiResponse(responseCode = "404", description = "해당 지갑 주소 없음", content = @Content),
            @ApiResponse(responseCode = "400", description = "송금 차단(자신에게 송금 or 잔액 부족)", content = @Content)
    })
    ResponseEntity<ResponseTransferDto> transfer(
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails customUserDetails,

            @Parameter(description = "송금 요청 정보 (받는 사람 주소, 보낼 금액)", required = true)
            @RequestBody RequestTransferDto requestTransferDto
    );

    @Operation(summary = "특정 회원 서비스 거래 내역 조회", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "거래 내역 조회 성공",
                    content = @Content(schema = @Schema(implementation = ResponseHistoryDto.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content),
            @ApiResponse(responseCode = "400", description = "거래 내역 조회 실패", content = @Content)
    })
    ResponseEntity<Page<ResponseHistoryDto>> findHistory(
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails customUserDetails,

            @Parameter(description = "검색 조건 (년)", example = "2026")
            @RequestParam(required = false) Integer year,

            @Parameter(description = "검색 조건 (월)", example = "1")
            @RequestParam(required = false) Integer month,

            @Parameter(description = "검색 조건 (타입) 미선택 시 전체")
            @RequestParam(required = false, defaultValue = "ALL") Type type,

            @ParameterObject Pageable pageable
    );
}
