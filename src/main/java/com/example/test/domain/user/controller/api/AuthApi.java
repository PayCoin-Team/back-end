package com.example.test.domain.user.controller.api;

import com.example.test.domain.user.dto.request.RequestUserDto;
import com.example.test.global.security.LoginFilter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Authentication API", description = "인증 관련 API (로그인, 로그아웃, 회원가입, 중복확인)")
public interface AuthApi {

    @Operation(summary = "로그인", description = "아이디와 비밀번호를 입력하여 로그인합니다. " +
            "(성공 시 Header에 Access Token, HttpOnlys Cookie에 Refresh Token 발급)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공 (Authorization Header 확인)", content = @Content),
            @ApiResponse(responseCode = "401", description = "로그인 실패 (아이디 또는 비밀번호 불일치)", content = @Content)
    })
    void login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "로그인 정보", required = true)
            @RequestBody LoginFilter.LoginDto loginDto
    );

    @Operation(summary = "로그아웃", description = "로그아웃을 수행합니다.(Header의 토큰을 프론트단에서 삭제 필요)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공", content = @Content),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content)
    })
    void logout();

    @Operation(summary = "회원가입", description = "새로운 회원을 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원가입 성공",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "입력값 유효성 검증 실패 (비밀번호 형식 등)", content = @Content),
            @ApiResponse(responseCode = "409", description = "이미 존재하는 아이디", content = @Content)
    })
    ResponseEntity<String> join(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "회원가입 정보 (아이디, 비번, 이름 등)", required = true)
            @RequestBody RequestUserDto requestUserDto
    );

    @Operation(summary = "아이디 중복 확인", description = "회원가입 시 아이디 중복 여부를 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공 (true: 중복됨 / false: 사용 가능)",
                    content = @Content(schema = @Schema(implementation = Boolean.class)))
    })
    ResponseEntity<Boolean> checkUsername(
            @Parameter(description = "확인할 아이디", required = true, example = "test1234")
            @RequestParam String username
    );

    @Operation(summary = "아이디 찾기", description = "이메일을 통해 가입된 사용자의 아이디를 메일로 발송합니다.")
    ResponseEntity<String> findId(@RequestParam String email);

    @Operation(summary = "비밀번호 재설정 - 인증번호 발송", description = "비밀번호 재설정을 위한 인증번호를 이메일로 발송합니다.")
    ResponseEntity<String> sendCode(@RequestParam String email);

    @Operation(summary = "비밀번호 재설정 - 인증번호 확인", description = "메일로 받은 인증번호가 유효한지 확인합니다.")
    ResponseEntity<String> verifyCode(@RequestParam String email, @RequestParam String code);

    @Operation(summary = "비밀번호 재설정 - 실제 변경", description = "인증 성공 후 새로운 비밀번호로 변경합니다.")
    ResponseEntity<String> resetPassword(@RequestParam String email, @RequestParam String newPassword);

}
