package com.example.test.domain.exchange.controller.api;

import com.example.test.domain.exchange.dto.response.ChartDataDto;
import com.example.test.domain.exchange.dto.response.ExchangeRateResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "Exchange API", description = "실시간 환율 조회 및 환산 관련 API")
public interface ExchangeRateApi {

    @Operation(summary = "실시간 환율 리스트 조회", description = "DB에 폴링된 최신 환율 정보 리스트를 가져옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = ExchangeRateResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content)
    })
    @GetMapping("/rates")
    ResponseEntity<List<ExchangeRateResponseDto>> getAllRates();


    @Operation(summary = "USDT 환산 계산기", description = "입력한 USDT 금액을 특정 국가의 통화 가치로 환산합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "환산 성공"),
            @ApiResponse(responseCode = "400", description = "지원하지 않는 통화 코드", content = @Content)
    })
    @GetMapping("/convert")
    ResponseEntity<BigDecimal> convert(
            @Parameter(description = "환산할 USDT 금액", required = true, example = "100.5")
            @RequestParam BigDecimal amount,

            @Parameter(description = "목표 통화 코드 (KRW, JPY 등)", required = true, example = "KRW")
            @RequestParam String target
    );

    @Operation(summary = "USDT 시세 차트 데이터 조회", description = "기간별(1D, 1W, 1M, 1Y) USDT 가격 데이터를 리스트로 반환합니다.")
    @GetMapping("/chart")
    ResponseEntity<List<ChartDataDto>> getChartData(
            @Parameter(description = "조회 기간 (1D, 1W, 1M, 1Y)", example = "1D")
            @RequestParam String period
    );
}