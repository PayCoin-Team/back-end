package com.example.test.domain.userwallet.service;

import com.example.test.domain.externalWallet.entity.ExternalWallet;
import com.example.test.domain.externalWallet.repository.ExternalWalletRepository;
import com.example.test.domain.userwallet.dto.response.ResponseUserWalletDto;
import com.example.test.domain.user.entity.User;
import com.example.test.domain.userwallet.entity.UserWallet;
import com.example.test.domain.userwallet.repository.UserWalletRepository;
import com.example.test.global.exception.CustomException;
import com.example.test.global.exception.error.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserWalletService {

    private final UserWalletRepository userWalletRepository;
    private final ExternalWalletRepository externalWalletRepository;

    // 사용자 내부 지갑 조회
    public ResponseUserWalletDto findUserWallet(Long userId) {
        // 1. 내부 지갑 조회
        UserWallet userWallet = userWalletRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_WALLET_NOT_FOUND));

        // 2. 외부 지갑 조회 (가장 최근에 등록된 주소 하나만 가져옴)
        String externalAddress = externalWalletRepository.findAllByUserId(userId)
                .stream()
                .findFirst() // 여러 개가 있다면 첫 번째 것을 선택
                .map(ExternalWallet::getAddress)
                .orElse(null); // 등록된 지갑이 없으면 null

        // 3. DTO 반환
        return ResponseUserWalletDto.from(userWallet, externalAddress);
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

    // public 지갑 주소로 사용자 이름 확인 (입금 전 확인용)
    public String verifyPublicAddress(String publicAddress) {
        UserWallet wallet = userWalletRepository.findByPublicAddress(publicAddress)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_WALLET_NOT_FOUND));

        // 사용자의 전체 이름을 반환
        return wallet.getUser().getName();
    }
}
