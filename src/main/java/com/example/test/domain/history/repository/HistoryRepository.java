package com.example.test.domain.history.repository;

import com.example.test.domain.history.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<History, Long> {
}
