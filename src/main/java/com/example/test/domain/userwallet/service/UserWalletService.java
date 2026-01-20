package com.example.test.domain.userwallet.service;

import com.example.test.domain.history.dto.response.ResponseUserWalletDto;
import com.example.test.domain.user.entity.User;
import com.example.test.domain.userwallet.entity.UserWallet;
import com.example.test.domain.userwallet.repository.UserWalletRepository;
import com.example.test.global.exception.CustomException;
import com.example.test.global.exception.error.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UserWalletService {

    private final UserWalletRepository userWalletRepository;

    public ResponseUserWalletDto findUserWallet(Long userId) {

        UserWallet userWallet = userWalletRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_WALLET_NOT_FOUND));

        return ResponseUserWalletDto.dtoToEntity(userWallet);
    }

    // 내부 지갑 생성
    public UserWallet generate(User user) {

        String publicAddress;

        // 공개 주소 생성 (중복 X)
        do{
            publicAddress = generateRandomAddress();
        } while (userWalletRepository.existsByPublicAddress(publicAddress));

        return UserWallet.builder()
                .user(user)
                .publicAddress(publicAddress)
                .build();
    }

    // 내부 지갑 공개 주소 생성
    private String generateRandomAddress() {

        String uuid = UUID.randomUUID().toString().toUpperCase();
        return uuid.substring(0, 4) + "-" + uuid.substring(4, 8);
    }
}
