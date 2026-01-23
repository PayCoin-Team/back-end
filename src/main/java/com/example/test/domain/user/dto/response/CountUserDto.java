package com.example.test.domain.user.dto.response;

public record CountUserDto(
        Long userCount, // 회원 수
        Long withdrawCount // 탈퇴한 회원 수
) {

    public static CountUserDto from(Long userCount, Long withdrawCount) {

        return new CountUserDto(userCount, withdrawCount);
    }
}
