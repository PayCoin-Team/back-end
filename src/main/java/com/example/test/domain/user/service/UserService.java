package com.example.test.domain.user.service;

import com.example.test.domain.user.dto.request.RequestUserDto;
import com.example.test.domain.user.dto.response.ResponseUserDto;
import com.example.test.domain.user.entity.User;
import com.example.test.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public void saveUser(RequestUserDto requestUserDto) {

        if(userRepository.existsByUsername(requestUserDto.username())) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }

        // 비밀번호 불일치
        if(!requestUserDto.password().equals(requestUserDto.checkPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String encodedPassword = passwordEncoder.encode(requestUserDto.password());
        User user = requestUserDto.dtoToEntity(encodedPassword);
        userRepository.save(user);
    }

    public ResponseUserDto findUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return ResponseUserDto.dtoToEntity(user);
    }

    public boolean checkUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}
