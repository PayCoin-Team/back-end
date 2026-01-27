package com.example.test.domain.history.repository.querydsl;

import com.example.test.domain.admin.dto.response.ResponseHistoryCounts;
import com.example.test.domain.history.entity.History;
import com.example.test.domain.history.enums.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface HistoryRepositoryCustom {

    Page<History> searchHistories(
            Long userId,
            Integer year,
            Integer month,
            Type type,
            Pageable pageable
    );

    List<Long> findActiveSenderIds(LocalDateTime start, LocalDateTime end);
    List<Long> findActiveReceiverIds(LocalDateTime start, LocalDateTime end);

    ResponseHistoryCounts historyTodayCounts(LocalDateTime start, LocalDateTime end);
}
