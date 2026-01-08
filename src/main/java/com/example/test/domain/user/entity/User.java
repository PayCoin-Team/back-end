package com.example.test.domain.user.entity;

import com.example.test.domain.user.enums.Role;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.role = Role.ROLE_USER;
    }
}
