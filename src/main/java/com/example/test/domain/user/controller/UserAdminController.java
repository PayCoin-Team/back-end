package com.example.test.domain.user.controller;

import com.example.test.domain.user.controller.api.UserAdminApi;
import com.example.test.domain.user.dto.response.CountUserDto;
import com.example.test.domain.user.service.UserAdminService;
import com.example.test.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class UserAdminController implements UserAdminApi {

    private final UserAdminService userAdminService;



    // 관리자 전용 회원 수 조회(일반 회원, 탈퇴한 회원)
    @Override
    @GetMapping("/users/count")
    public ResponseEntity<CountUserDto> countRoleUser() {

        return ResponseEntity.ok(userAdminService.countRoleUser());
    }
}
