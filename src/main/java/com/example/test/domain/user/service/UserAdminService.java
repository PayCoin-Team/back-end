package com.example.test.domain.user.service;

import com.example.test.domain.user.dto.response.CountUserDto;
import com.example.test.domain.user.enums.Role;
import com.example.test.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserAdminService {

    private final UserRepository userRepository;

    // 상태 별 회원 수
    public CountUserDto countRoleUser() {

        // 일반 회원, 탈퇴한 회원 수
        Long userCount = userRepository.countByRole(Role.ROLE_USER);
        Long withdrawCount = userRepository.countByRole(Role.WITH_DRAW);

        return CountUserDto.from(userCount, withdrawCount);
    }
}
