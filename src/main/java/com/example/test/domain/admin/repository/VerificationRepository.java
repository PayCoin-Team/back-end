package com.example.test.domain.admin.repository;

import com.example.test.domain.admin.entity.Verification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationRepository extends JpaRepository<Verification, Long> {
    Verification findFirstByOrderByIdDesc();
}