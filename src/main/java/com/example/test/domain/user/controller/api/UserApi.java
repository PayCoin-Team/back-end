package com.example.test.domain.user.controller.api;

import com.example.test.domain.user.dto.request.ChangePasswordDto;
import com.example.test.domain.user.dto.request.UpdateUserDto;
import com.example.test.domain.user.dto.response.ResponseUserDto;
import com.example.test.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "회원 관련 API", description = "회원 관련(조회, 수정, 탈퇴) 기능 API")
public interface UserApi {

    @Operation(summary = "회원 정보 조회", description = "회원 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 조회 성공",
                    content = @Content(schema = @Schema(implementation = ResponseUserDto.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content)
    })
    ResponseEntity<ResponseUserDto> findUser(
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails user
    );

    @Operation(summary = "회원 정보 수정", description = "회원 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원 정보 수정 성공",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content)
    })
    ResponseEntity<String> updateUser(
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails user,

            @Parameter(description = "수정할 회원 정보(수정할 값만 입력하면 됨.)")
            @RequestBody UpdateUserDto updateUserDto
    );

    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 탈퇴 성공",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content)
    })
    ResponseEntity<String> withdraw(
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails user
    );

    @Operation(summary = "비밀번호 재설정", description = "로그인 한 사용자 비밀번호 재설정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "비밀번호 재설정 성공",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "비밀번호 불일치", content = @Content),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content)
    })
    ResponseEntity<String> changePassword(
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails user,

            @Parameter(description = "비밀번호 재설정")
            @RequestBody @Valid ChangePasswordDto changePasswordDto

    );
}
