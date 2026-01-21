package com.example.test.domain.admin.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Admin Chat", description = "관리자 전용 데이터 분석 챗봇 API")
public interface AdminChatApi {

    @Operation(
            summary = "관리자 데이터 질의",
            description = "관리자가 CrossPay 시스템의 사용자 수, 거래 건수 등에 대해 자연어로 질문합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공적인 답변 생성", content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "403", description = "권한 부족 (ADMIN 권한 필요)"),
                    @ApiResponse(responseCode = "500", description = "AI 모델 응답 오류")
            }
    )
    String ask(@RequestBody String message);
}
