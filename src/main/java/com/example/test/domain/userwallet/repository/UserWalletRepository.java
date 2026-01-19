package com.example.test.domain.userwallet.repository;

import com.example.test.domain.userwallet.entity.UserWallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserWalletRepository extends JpaRepository<UserWallet, Long> {

    Boolean existsByPublicAddress (String publicAddress);
}
