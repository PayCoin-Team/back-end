package com.example.test.domain.user.dto.request;

import com.example.test.domain.user.entity.User;


public record RequestUserDto(
        String username,
        String password
) {
    public User dtoToEntity(String encodedPassword) {
        return User.builder()
                .username(username)
                .password(encodedPassword)
                .build();
    }
}
