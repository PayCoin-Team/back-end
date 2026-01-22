package com.example.test.domain.polling.controller.api;

import com.example.test.domain.polling.dto.ServerBalnceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "Tron API", description = "TronGrid 관련 API")
public interface TronAPI {
    @Operation(summary = "서버 지갑 잔고 조회", description = "서버 지갑의 USDT과 TRX 잔액을 불러옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = ServerBalnceResponse.class))),
            @ApiResponse(responseCode = "500", description = "API로 데이터를 가져오지 못했습니다.", content = @Content)
    })
    @GetMapping("/balance")
    ResponseEntity<ServerBalnceResponse> getServerBalance();
}
