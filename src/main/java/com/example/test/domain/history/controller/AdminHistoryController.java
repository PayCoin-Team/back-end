package com.example.test.domain.history.controller;

import com.example.test.domain.history.controller.api.AdminHistoryApi;
import com.example.test.domain.history.dto.response.ResponseAdminHistoryDto;
import com.example.test.domain.history.dto.response.ResponseHistoryDto;
import com.example.test.domain.history.repository.HistoryRepository;
import com.example.test.domain.history.service.AdminHistoryService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminHistoryController implements AdminHistoryApi {

    private final AdminHistoryService adminHistoryService;

    // 서비스 내 모든 거래내역 조회
    @Override
    @GetMapping("/histories")
    public ResponseEntity<Page<ResponseAdminHistoryDto>> findAllHistory(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            @ParameterObject Pageable pageable
    ) {

        return ResponseEntity.ok(adminHistoryService.findAllHistory(year, month, pageable));
    }
}
