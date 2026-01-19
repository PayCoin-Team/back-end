package com.example.test.domain.nonce.repository;

import com.example.test.domain.nonce.entity.Nonce;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NonceRepository extends JpaRepository<Nonce, Long> {
    // 서명 검증 시 Nonce 값으로 조회
    Optional<Nonce> findByNonce(String nonce);

    // 동일 유저의 이전 요청 데이터 정리용
    void deleteByUserId(Long userId);
}
