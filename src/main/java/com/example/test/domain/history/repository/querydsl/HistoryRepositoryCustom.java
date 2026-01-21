package com.example.test.domain.history.repository.querydsl;

import com.example.test.domain.history.entity.History;
import com.example.test.domain.history.enums.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HistoryRepositoryCustom {

    Page<History> searchHistories(
            Long userId,
            Integer year,
            Integer month,
            Type type,
            Pageable pageable
    );
}
