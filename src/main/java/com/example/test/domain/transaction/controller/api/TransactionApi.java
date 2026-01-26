package com.example.test.domain.transaction.controller.api;

import com.example.test.domain.transaction.dto.request.RequestConfirmDepositDto;
import com.example.test.domain.transaction.enums.Type;
import com.example.test.domain.transaction.dto.request.RequestTransactionDto;
import com.example.test.domain.transaction.dto.response.ResponseTransactionDto;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Transaction API", description = "외부 지갑(거래 내역 조회, 입/출금 신청) 관련 API")
public interface TransactionApi {

    @Operation(summary = "외부 지갑 입금 요청", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "외부 지갑 입금 성공",
                    content = @Content(schema = @Schema(implementation = ResponseTransactionDto.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content),
            @ApiResponse(responseCode = "404", description = "해당 지갑 주소 없음", content = @Content),
            @ApiResponse(responseCode = "400", description = "송금 차단(자신에게 송금 or 잔액 부족)", content = @Content)
    })
    ResponseEntity<ResponseTransactionDto> deposit(
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails customUserDetails,

            @Parameter(description = "출금 요청 입력(금액, 사용자 외부 지갑 주소)", required = true)
            @RequestBody RequestTransactionDto requestTransactionDto
    );

    @Operation(summary = "외부 지갑 출금 요청", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "외부 지갑 출금 성공",
                    content = @Content(schema = @Schema(implementation = ResponseTransactionDto.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content),
            @ApiResponse(responseCode = "404", description = "해당 지갑 주소 없음", content = @Content),
            @ApiResponse(responseCode = "400", description = "송금 차단(자신에게 송금 or 잔액 부족)", content = @Content)
    })
    ResponseEntity<ResponseTransactionDto> withdraw(
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails user,

            @Parameter(description = "출금 요청 입력(금액, 사용자 외부 지갑 주소)", required = true)
            @RequestBody RequestTransactionDto requestTransactionDto
    );

    @Operation(summary = "입/출금 거래 내역 조회", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "거래 내역 조회 성공",
                    content = @Content(schema = @Schema(implementation = ResponseTransactionDto.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content),
            @ApiResponse(responseCode = "400", description = "거래 내역 조회 실패", content = @Content)
    })
    ResponseEntity<Page<ResponseTransactionDto>> findTransaction(
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails customUserDetails,

            @Parameter(description = "검색 조건 (년)", example = "2026")
            @RequestParam(required = false) Integer year,

            @Parameter(description = "검색 조건 (월)", example = "1")
            @RequestParam(required = false) Integer month,

            @Parameter(description = "검색 조건 (타입) 미선택 시 전체")
            @RequestParam(required = false) Type type,

            @ParameterObject Pageable pageable
    );

    @Operation(summary = "외부 지갑 입금 요청", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "입금 상태",
                    content = @Content(schema = @Schema(implementation = ResponseTransactionDto.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content),
            @ApiResponse(responseCode = "404", description = "해당 지갑 주소 없음", content = @Content),
    })
    ResponseEntity<ResponseTransactionDto> confirmDeposit(
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails customUserDetails,

            @Parameter(description = "transactionId", required = true)
            Long transactionId,

            @Parameter(description = "출금 확인 입력(txid)", required = true)
            @RequestBody RequestConfirmDepositDto req
            );

}
