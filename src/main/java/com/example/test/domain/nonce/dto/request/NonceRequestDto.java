package com.example.test.domain.nonce.dto.request;

import jakarta.validation.constraints.NotBlank;

// Nonce 발급용 요청 DTO
public record NonceRequestDto(
        @NotBlank String address
) {
}
