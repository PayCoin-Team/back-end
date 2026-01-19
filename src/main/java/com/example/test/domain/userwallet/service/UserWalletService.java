package com.example.test.domain.userwallet.service;

import com.example.test.domain.user.entity.User;
import com.example.test.domain.userwallet.entity.UserWallet;
import com.example.test.domain.userwallet.repository.UserWalletRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UserWalletService {

    private final UserWalletRepository userWalletRepository;

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

    private String generateRandomAddress() {

        String uuid = UUID.randomUUID().toString().toUpperCase();
        return uuid.substring(0, 4) + "-" + uuid.substring(4, 8);
    }
}
