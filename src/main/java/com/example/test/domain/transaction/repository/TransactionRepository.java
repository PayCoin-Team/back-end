package com.example.test.domain.transaction.repository;

import com.example.test.domain.transaction.entity.Status;
import com.example.test.domain.transaction.entity.Transaction;
import com.example.test.domain.transaction.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByTxid(String txId);

    @Query("SELECT COALESCE(SUM(t.fee), 0) from Transaction t where t.status = :status")
    BigDecimal sumFees(Status status);
}