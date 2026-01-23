package com.example.test.domain.admin.service;

import com.example.test.domain.admin.dto.FeeSummaryResponse;
import com.example.test.domain.transaction.enums.Status;
import com.example.test.domain.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final TransactionRepository transactionRepository;

    public FeeSummaryResponse getFees(){

        //오늘 날짜의 자정
        LocalDateTime today = LocalDate.now().atStartOfDay();

        BigDecimal totalFees = transactionRepository.sumFees(Status.COMPLETED);
        BigDecimal yesterdayFees = transactionRepository.sumFeesBeforeDate(today);

        return new FeeSummaryResponse(totalFees, yesterdayFees);
    }
}
