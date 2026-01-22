package com.example.test.domain.transaction.repository;

import com.example.test.domain.transaction.entity.Transaction;
import com.example.test.domain.transaction.repository.impl.TransactionRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long>, TransactionRepositoryCustom {
}