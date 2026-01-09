package com.example.test.domain.user.dto.response;

import com.example.test.domain.user.entity.User;
import lombok.Builder;

@Builder
public record ResponseUserDto(
        String username,
        String nickName
) {
    public static ResponseUserDto dtoToEntity(User user) {
        return ResponseUserDto.builder()
                .username((user.getUsername()))
                .nickName(user.getNickName())
                .build();
    }
}
