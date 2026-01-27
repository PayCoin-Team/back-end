package com.example.test.domain.admin.controller;

import com.example.test.domain.admin.controller.api.AdminControllerApi;
import com.example.test.domain.admin.dto.FeeSummaryResponse;
import com.example.test.domain.admin.dto.response.ResponseAllTransactionDto;
import com.example.test.domain.admin.dto.response.ResponseHistoryCounts;
import com.example.test.domain.admin.dto.response.UserTransferDto;
import com.example.test.domain.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController implements AdminControllerApi {
    private final AdminService adminService;

    @Override
    public ResponseEntity<FeeSummaryResponse> getFees() {
        return ResponseEntity.ok(adminService.getFees());
    }

    // 서비스 내 모든 입/출금 내역 조회
    @Override
    @GetMapping("/transactions")
    public ResponseEntity<Page<ResponseAllTransactionDto>> findAllTransaction(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(adminService.findAllTransaction(year, month, pageable));
    }

    // 특정 회원 모든 거래내역(내부 거래, 입/출금) 조회
    @Override
    @GetMapping("/transfer/{userId}")
    public ResponseEntity<Page<UserTransferDto>> userTransfer(
            @PathVariable("userId") Long userId,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            @ParameterObject Pageable pageable) {

        return ResponseEntity.ok(adminService.userTransfer(userId, year, month, pageable));
    }

    // 오늘 거래한 사용자 수
    @Override
    @GetMapping("/today/transfer")
    public ResponseEntity<Long> findTodayActiveUserCount() {

        return ResponseEntity.ok(adminService.findTodayActiveUserCount());
    }

    // 오늘 거래된 횟수 및 금액 조회
    @Override
    @GetMapping("/today/history")
    public ResponseEntity<ResponseHistoryCounts> historyTodayCounts() {

        return ResponseEntity.ok(adminService.historyTodayCounts());
    }

    // 수수료 조회
    @Override
    @GetMapping("/find/fee")
    public ResponseEntity<Page<ResponseAllTransactionDto>> findFee(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            @ParameterObject Pageable pageable
    ) {

        return ResponseEntity.ok(adminService.findFee(year, month, pageable));
    }
}
