package com.example.test.domain.admin.controller.api;

import com.example.test.domain.admin.dto.FeeSummaryResponse;
import com.example.test.domain.admin.dto.response.ResponseAllTransactionDto;
import com.example.test.domain.admin.dto.response.UserTransferDto;
import com.example.test.domain.exchange.dto.response.ExchangeRateResponseDto;
import com.example.test.domain.transaction.enums.Type;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Admin", description = "관리 API")
public interface AdminControllerApi {
    @Operation(summary = "전일 수수료와 총 수수료 조회", description = "DB에서 전일 수수료와 총 수수료를 가져옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = FeeSummaryResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content)
    })
    @GetMapping("/rates")
    ResponseEntity<FeeSummaryResponse> getFees();

    @Operation(summary = "관리자 전용 외부 거래 내역 조회", description = "모든 입/출금 거래내역을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = ResponseAllTransactionDto.class))),
            @ApiResponse(responseCode = "403", description = "일반 회원 접근 차단", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content)
    })
    ResponseEntity<Page<ResponseAllTransactionDto>> findAllTransaction(
            @Parameter(description = "검색 조건 (년)", example = "2026")
            @RequestParam(required = false) Integer year,

            @Parameter(description = "검색 조건 (월)", example = "01")
            @RequestParam(required = false) Integer month,

            @ParameterObject Pageable pageable
    );

    @Operation(summary = "관리자 전용 특정 회원 내/외부 거래 내역 조회", description = "특정 회원의 모든 거래내역을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = UserTransferDto.class))),
            @ApiResponse(responseCode = "403", description = "일반 회원 접근 차단", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content)
    })
    ResponseEntity<Page<UserTransferDto>> userTransfer(
            @Parameter(description = "유저 id", example = "1")
            @PathVariable("userId") Long userId,

            @Parameter(description = "검색 조건 (년)", example = "2026")
            @RequestParam(required = false) Integer year,

            @Parameter(description = "검색 조건 (월)", example = "01")
            @RequestParam(required = false) Integer month,

            @ParameterObject Pageable pageable
    );

    @Operation(summary = "오늘 거래한 사용자 수 조회", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "403", description = "일반 회원 접근 차단", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content)
    })
    ResponseEntity<Long> findTodayActiveUserCount();
}
