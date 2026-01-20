package com.example.test.domain.polling.repository;

import com.example.test.domain.polling.model.Polling;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PollingRepository extends JpaRepository<Polling, Long> {
}