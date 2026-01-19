package com.example.test.domain.user.service;

import com.example.test.domain.user.dto.request.RequestUserDto;
import com.example.test.domain.user.dto.request.UpdateUserDto;
import com.example.test.domain.user.dto.response.ResponseUserDto;
import com.example.test.domain.user.entity.User;
import com.example.test.domain.user.repository.UserRepository;
import com.example.test.global.exception.CustomException;
import com.example.test.global.exception.error.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 회원 정보 조회
    public ResponseUserDto findUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return ResponseUserDto.dtoToEntity(user);
    }

    // 회원 정보 변경
    public void updateUser(Long userId, UpdateUserDto updateUserDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.updateProfile(updateUserDto);
    }

    // 회원 탈퇴
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.deleteProfile();
    }
}
