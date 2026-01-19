package com.example.test.global.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 서버 관련 오류
    INTERNAL_SERVER_ERROR(500, "내부 서버 오류입니다."),
    // 존재하지 않는 사용자
    USER_NOT_FOUND(404, "존재하지 않는 사용자입니다."),
    DUPLICATE_USERNAME(409, "이미 존재하는 아이디입니다."),
    PASSWORD_NOT_MATCH(400, "비밀번호가 일치하지 않습니다."),

    // 지갑 연동 관련 오류
    NONCE_NOT_FOUND(404,  "유효한 인증 요청을 찾을 수 없습니다."),
    EXPIRED_NONCE(400,  "인증 시간이 만료되었습니다. 다시 시도해 주세요."),
    INVALID_SIGNATURE(400, "서명 검증에 실패했습니다. 지갑 주소를 확인하세요."),
    ALREADY_LINKED_WALLET(409, "이미 다른 계정에 연동된 지갑 주소입니다."),
    INVALID_USER_ATTEMPT(403, "잘못된 유저의 접근입니다.");

    private final int status;
    private final String message;
}
