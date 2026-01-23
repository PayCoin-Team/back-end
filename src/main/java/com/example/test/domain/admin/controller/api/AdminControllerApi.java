package com.example.test.domain.admin.controller.api;

import com.example.test.domain.admin.dto.FeeSummaryResponse;
import com.example.test.domain.exchange.dto.response.ExchangeRateResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

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
}
