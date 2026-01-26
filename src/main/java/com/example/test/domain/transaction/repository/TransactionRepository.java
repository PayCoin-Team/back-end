package com.example.test.domain.transaction.repository;

import com.example.test.domain.transaction.enums.Status;
import com.example.test.domain.transaction.entity.Transaction;
import com.example.test.domain.transaction.repository.impl.TransactionRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long>, TransactionRepositoryCustom {
    Optional<Transaction> findByTxid(String txId);

    @Query("SELECT COALESCE(SUM(t.fee), 0) from Transaction t where t.status = :status")
    BigDecimal sumFees(Status status);

    @Query("SELECT COALESCE(SUM(t.fee), 0) FROM Transaction t WHERE t.createdAt < :date")
    BigDecimal sumFeesBeforeDate(LocalDateTime date);

    Optional<Transaction> findFirstByFromAddressAndToAddressAndAmountAndStatusAndTxidIsNullOrderByCreatedAtAsc(String fromAddress, String fromAddress1, BigDecimal amount, Status status);
}