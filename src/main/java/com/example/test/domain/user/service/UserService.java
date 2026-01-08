package com.example.test.domain.user.service;

import com.example.test.domain.user.dto.request.RequestUserDto;
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

        String encodedPassword = passwordEncoder.encode(requestUserDto.password());
        User user = requestUserDto.dtoToEntity(encodedPassword);
        userRepository.save(user);
    }
}
