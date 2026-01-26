package com.example.test.domain.admin.service;

import com.example.test.domain.admin.dto.FeeSummaryResponse;
import com.example.test.domain.admin.dto.response.ResponseAllTransactionDto;
import com.example.test.domain.admin.dto.response.UserTransferDto;
import com.example.test.domain.history.entity.History;
import com.example.test.domain.history.repository.HistoryRepository;
import com.example.test.domain.transaction.entity.Transaction;
import com.example.test.domain.transaction.enums.Status;
import com.example.test.domain.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final TransactionRepository transactionRepository;
    private final HistoryRepository historyRepository;

    public FeeSummaryResponse getFees() {

        //오늘 날짜의 자정
        LocalDateTime today = LocalDate.now().atStartOfDay();

        BigDecimal totalFees = transactionRepository.sumFees(Status.COMPLETED);
        BigDecimal yesterdayFees = transactionRepository.sumFeesBeforeDate(today);

        return new FeeSummaryResponse(totalFees, yesterdayFees);
    }

    // 서비스 내 모든 입/출금 내역 조회
    @Transactional(readOnly = true)
    public Page<ResponseAllTransactionDto> findAllTransaction(
            Integer year,
            Integer month,
            Pageable pageable
    ) {
        Page<Transaction> transactions = transactionRepository.searchTransaction(null, year, month, null, pageable);
        return transactions.map(transaction -> ResponseAllTransactionDto.from(transaction));
    }

    // 특정 회원 모든 거래 내역(내부 거래, 입/출금) 조회
    @Transactional(readOnly = true)
    public Page<UserTransferDto> userTransfer(
            Long userId,
            Integer year,
            Integer month,
            Pageable pageable
    ) {

        Pageable unlimited = PageRequest.of(0, Integer.MAX_VALUE);

        // 입/출금 내역 조회
        List<Transaction> transactions = transactionRepository
                .searchTransaction(userId, year, month, null, unlimited)
                .getContent();

        // 내부 거래 내역 조회
        List<History> histories = historyRepository
                .searchHistories(userId, year, month, null, unlimited)
                .getContent();

        List<UserTransferDto> mergedList = new ArrayList<>();

        for (Transaction tx : transactions) {
            mergedList.add(UserTransferDto.from(tx));
        }
        for (History h : histories) {
            mergedList.add(UserTransferDto.from(h, userId));
        }

        mergedList.sort(Comparator.comparing(UserTransferDto::time).reversed());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), mergedList.size());

        if (start > mergedList.size()) {
            return new PageImpl<>(Collections.emptyList(), pageable, mergedList.size());
        }

        List<UserTransferDto> pagedContent = mergedList.subList(start, end);

        return new PageImpl<>(pagedContent, pageable, mergedList.size());
    }
}
