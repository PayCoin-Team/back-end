package com.example.test.domain.nonce.dto.request;

import jakarta.validation.constraints.NotBlank;

// 서명 검증 및 연동 완료용 DTO
public record VerifyRequestDto(
        @NotBlank
        String address,
        @NotBlank
        String nonce,
        @NotBlank
        String signature
) {
}
