package com.example.test.domain.user.repository.impl;

import com.example.test.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom {

    Page<User> findAllUser(
            Pageable pageable,
            String keyword
    );
}
