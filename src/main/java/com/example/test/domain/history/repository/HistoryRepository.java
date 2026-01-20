package com.example.test.domain.history.repository;

import com.example.test.domain.history.entity.History;
import com.example.test.domain.history.repository.querydsl.HistoryRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<History, Long>, HistoryRepositoryCustom {
}
