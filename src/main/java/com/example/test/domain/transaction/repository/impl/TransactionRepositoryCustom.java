package com.example.test.domain.transaction.repository.impl;

import com.example.test.domain.transaction.enums.Type;
import com.example.test.domain.transaction.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepositoryCustom {

    Page<Transaction> searchTransaction(
            Long userId,
            Integer year,
            Integer month,
            Type type,
            Pageable pageable
    );

    List<Long> findActiveUserIds(LocalDateTime start, LocalDateTime end);
}
