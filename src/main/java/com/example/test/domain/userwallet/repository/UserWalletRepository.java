package com.example.test.domain.userwallet.repository;

import com.example.test.domain.userwallet.entity.UserWallet;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserWalletRepository extends JpaRepository<UserWallet, Long> {

    Boolean existsByPublicAddress (String publicAddress);

    @Lock(LockModeType.PESSIMISTIC_WRITE) // 비관적 락 조회
    @Query("select w from UserWallet w where w.user.id = :userId")
    Optional<UserWallet> findByUserIdWithLock (@Param("userId") Long userId);

    Optional<UserWallet> findByPublicAddress (String publicAddress);
}
