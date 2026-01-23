package com.example.test.domain.admin.service;

import com.example.test.domain.admin.dto.FeeSummaryResponse;
import com.example.test.domain.admin.dto.response.ResponseAllTransactionDto;
import com.example.test.domain.transaction.entity.Transaction;
import com.example.test.domain.transaction.enums.Status;
import com.example.test.domain.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final TransactionRepository transactionRepository;

    public FeeSummaryResponse getFees() {

        //오늘 날짜의 자정
        LocalDateTime today = LocalDate.now().atStartOfDay();

        BigDecimal totalFees = transactionRepository.sumFees(Status.COMPLETED);
        BigDecimal yesterdayFees = transactionRepository.sumFeesBeforeDate(today);

        return new FeeSummaryResponse(totalFees, yesterdayFees);
    }

    public Page<ResponseAllTransactionDto> findAllTransaction(
            Integer year,
            Integer month,
            Pageable pageable
    ) {
        Page<Transaction> transactions = transactionRepository.searchTransaction(null, year, month, null, pageable);
        return transactions.map(transaction -> ResponseAllTransactionDto.from(transaction));
    }
}
