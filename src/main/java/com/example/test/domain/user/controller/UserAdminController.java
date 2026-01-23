package com.example.test.domain.user.controller;

import com.example.test.domain.user.controller.api.UserAdminApi;
import com.example.test.domain.user.dto.response.CountUserDto;
import com.example.test.domain.user.dto.response.UserStatusResponse;
import com.example.test.domain.user.service.UserAdminService;
import com.example.test.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    // 관리자 전용 회원 상태 변경
    @Override
    @PatchMapping("/users/{userId}/status")
    public ResponseEntity<UserStatusResponse> changeUserStatus(
            @PathVariable("userId") Long id) {

        return ResponseEntity.status(HttpStatus.OK).body(userAdminService.userStatusChange(id));
    }
}
