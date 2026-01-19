package com.example.test.domain.user.service;

import com.example.test.domain.user.dto.request.RequestUserDto;
import com.example.test.domain.user.entity.User;
import com.example.test.domain.user.repository.UserRepository;
import com.example.test.global.exception.CustomException;
import com.example.test.global.exception.error.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // 회원가입
    public void saveUser(RequestUserDto requestUserDto) {
        // 아이디 중복 예외
        if(userRepository.existsByUsername(requestUserDto.username())) {
            throw new CustomException(ErrorCode.DUPLICATE_USERNAME);
        }

        // 비밀번호 불일치
        if(!requestUserDto.password().equals(requestUserDto.checkPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCH);
        }

        String encodedPassword = passwordEncoder.encode(requestUserDto.password());
        User user = requestUserDto.dtoToEntity(encodedPassword);
        userRepository.save(user);
    }

    // 아이디 중복 확인
    public boolean checkUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}
