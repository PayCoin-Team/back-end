package com.example.test.domain.user.controller.api;

import com.example.test.domain.user.dto.response.CountUserDto;
import com.example.test.domain.user.dto.response.UserStatusResponse;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "관리자 전용 회원 관리 API", description = "관리자용 회원 관리(사용자 수, 목록, 탈퇴)")
public interface UserAdminApi {

    @Operation(summary = "가입한 회원 총 수", description = "가입한 회원의 수 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 수 조회 성공",
                    content = @Content(schema = @Schema(implementation = CountUserDto.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content),
            @ApiResponse(responseCode = "403", description = "일반 회원 접근 차단", content = @Content)
    })
    ResponseEntity<CountUserDto> countRoleUser();

    @Operation(summary = "회원 상태 변경", description = "해당 사용자 정지 상태로 변경한다.(정지 상태인 회원은 정지 해제 시킨다.)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원 상태 변경 성공",
                    content = @Content(schema = @Schema(implementation = UserStatusResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content),
            @ApiResponse(responseCode = "403", description = "일반 회원 접근 차단", content = @Content)
    })
    ResponseEntity<UserStatusResponse> changeUserStatus(
            @Parameter(description = "상태 변경할 회원 id", required = true, example = "1")
            @PathVariable("userId") Long id
    );
}
