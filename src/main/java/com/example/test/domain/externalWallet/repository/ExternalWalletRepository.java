package com.example.test.domain.externalWallet.repository;

import com.example.test.domain.externalWallet.entity.ExternalWallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExternalWalletRepository extends JpaRepository<ExternalWallet, Long> {
    // 특정 유저에게 등록된 지갑 리스트 조회
    List<ExternalWallet> findAllByUserId(Long userId);

    // 이미 등록된 주소인지 확인
    boolean existsByAddress(String address);
}
