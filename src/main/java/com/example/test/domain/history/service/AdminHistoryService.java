package com.example.test.domain.history.service;

import com.example.test.domain.history.dto.response.ResponseAdminHistoryDto;
import com.example.test.domain.history.dto.response.ResponseHistoryDto;
import com.example.test.domain.history.entity.History;
import com.example.test.domain.history.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminHistoryService {

    private final HistoryRepository historyRepository;

    // 서비스 내 모든 거래내역 조회
    public Page<ResponseAdminHistoryDto> findAllHistory(Integer year, Integer month, Pageable pageable) {

        Page<History> histories = historyRepository.searchHistories(null, year, month, null, pageable);

        return histories.map(history -> ResponseAdminHistoryDto.from(history));
    }
}
