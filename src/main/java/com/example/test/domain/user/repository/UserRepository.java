package com.example.test.domain.user.repository;

import com.example.test.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByUsername (String username);
    Optional<User> findByUsername (String username);

    // 이메일로 사용자 조회 기능
    Optional<User> findByEmail(String email);
}
