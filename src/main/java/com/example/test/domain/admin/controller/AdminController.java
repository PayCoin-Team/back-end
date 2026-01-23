package com.example.test.domain.admin.controller;

import com.example.test.domain.admin.controller.api.AdminControllerApi;
import com.example.test.domain.admin.dto.FeeSummaryResponse;
import com.example.test.domain.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
