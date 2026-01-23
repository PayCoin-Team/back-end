package com.example.test.domain.admin.controller;

import com.example.test.domain.admin.controller.api.AdminControllerApi;
import com.example.test.domain.admin.dto.FeeSummaryResponse;
import com.example.test.domain.admin.dto.response.ResponseAllTransactionDto;
import com.example.test.domain.admin.service.AdminService;
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
public class AdminController implements AdminControllerApi {
    private final AdminService adminService;

    @Override
    public ResponseEntity<FeeSummaryResponse> getFees() {
        return ResponseEntity.ok(adminService.getFees());
    }

    @Override
    @GetMapping("/admin/transactions")
    public ResponseEntity<Page<ResponseAllTransactionDto>> findAllTransaction(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(adminService.findAllTransaction(year, month, pageable));
    }
}
