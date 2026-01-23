package com.example.test.domain.user.service;

import com.example.test.domain.user.dto.response.CountUserDto;
import com.example.test.domain.user.dto.response.UserStatusResponse;
import com.example.test.domain.user.entity.User;
import com.example.test.domain.user.enums.Role;
import com.example.test.domain.user.repository.UserRepository;
import com.example.test.domain.userwallet.entity.UserWallet;
import com.example.test.domain.userwallet.repository.UserWalletRepository;
import com.example.test.global.exception.CustomException;
import com.example.test.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserAdminService {

    private final UserRepository userRepository;
    private final UserWalletRepository userWalletRepository;

    // 상태 별 회원 수
    public CountUserDto countRoleUser() {

        // 일반 회원, 탈퇴한 회원 수
        Long userCount = userRepository.countByRole(Role.ROLE_USER);
        Long withdrawCount = userRepository.countByRole(Role.WITH_DRAW);

        return CountUserDto.from(userCount, withdrawCount);
    }

    public UserStatusResponse userStatusChange(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        UserWallet userWallet = userWalletRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_WALLET_NOT_FOUND));

        if(user.getRole().equals(Role.ROLE_USER)) {
            user.setRole(Role.ROLE_BANNED); // 정지
        } else if (user.getRole().equals(Role.ROLE_BANNED)) {
            user.setRole(Role.ROLE_USER); // 정지 해제
        } else {
            throw new CustomException(ErrorCode.INVALID_USER_ATTEMPT);
        }

        return UserStatusResponse.from(user, userWallet);
    }
}
