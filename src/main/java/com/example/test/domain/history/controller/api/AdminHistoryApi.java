package com.example.test.domain.history.controller.api;

import com.example.test.domain.history.dto.response.ResponseAdminHistoryDto;
import com.example.test.domain.history.dto.response.ResponseHistoryDto;
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
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Admin", description = "관리 API")
public interface AdminHistoryApi {

    @Operation(summary = "서비스 모든 내부 거래 조회", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = ResponseAdminHistoryDto.class))),
            @ApiResponse(responseCode = "403", description = "일반 회원 접근 차단", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content)
    })
    ResponseEntity<Page<ResponseAdminHistoryDto>> findAllHistory(
            @Parameter(description = "검색 조건 (년)", example = "2026")
            @RequestParam(required = false) Integer year,

            @Parameter(description = "검색 조건 (월)", example = "01")
            @RequestParam(required = false) Integer month,

            @ParameterObject Pageable pageable
    );
}
