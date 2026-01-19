package com.example.test.domain.nonce.service;

import com.example.test.domain.externalWallet.entity.ExternalWallet;
import com.example.test.domain.externalWallet.repository.ExternalWalletRepository;
import com.example.test.domain.nonce.dto.request.NonceRequestDto;
import com.example.test.domain.nonce.dto.request.VerifyRequestDto;
import com.example.test.domain.nonce.dto.response.NonceResponseDto;
import com.example.test.domain.nonce.entity.Nonce;
import com.example.test.domain.nonce.repository.NonceRepository;
import com.example.test.domain.user.entity.User;
import com.example.test.domain.user.repository.UserRepository;
import com.example.test.global.exception.CustomException;
import com.example.test.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WalletVerifyService {
    private final NonceRepository nonceRepository;
    private final ExternalWalletRepository externalWalletRepository;
    private final UserRepository userRepository;

    // Nonce 생성 및 저장
    @Transactional
    public NonceResponseDto generateNonce(Long userId, NonceRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        nonceRepository.deleteByUserId(userId);

        String nonceString = "Login to CrossPay : " + UUID.randomUUID();

        Nonce nonce = Nonce.builder()
                .nonce(nonceString)
                .walletAddress(requestDto.address())
                .user(user)
                .expiryMinutes(5)
                .build();

        nonceRepository.save(nonce);

        return new NonceResponseDto(nonceString);
    }

    // 서명 검증 및 정식 연동
    @Transactional
    public void verifyAndLink(Long userId, VerifyRequestDto requestDto) {
        // 1. Noce 조회 및 기본 검증
        Nonce nonce = nonceRepository.findByNonce(requestDto.nonce())
                .orElseThrow(() -> new CustomException(ErrorCode.NONCE_NOT_FOUND));

        // 2. 요청 유저 일치 여부 확인
        if (!nonce.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.INVALID_USER_ATTEMPT);
        }

        // 3. 시간 만료 확인
        if (nonce.isExpired()) {
            nonceRepository.delete(nonce);
            throw new CustomException(ErrorCode.EXPIRED_NONCE);
        }

        // 4. 이미 등록된 지갑인지 체크
        if (externalWalletRepository.existsByAddress(requestDto.address())) {
            throw new CustomException(ErrorCode.ALREADY_LINKED_WALLET);
        }

        // 5. 서명 검증
        // 암호학적 서명 검증 필요 (Web3j)
        String recoveredAddress = requestDto.address();

        // 6. DB 주소 대조
        if (!recoveredAddress.equalsIgnoreCase(nonce.getWalletAddress())) {
            throw new CustomException(ErrorCode.INVALID_SIGNATURE);
        }

        // 7. 연동 완료 및 임시 데이터 파기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        externalWalletRepository.save(new ExternalWallet(user, recoveredAddress));
        nonceRepository.delete(nonce);
    }
}
