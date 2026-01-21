package com.example.test.domain.user.entity;

import com.example.test.domain.user.dto.request.UpdateUserDto;
import com.example.test.domain.user.enums.Role;
import com.example.test.domain.userwallet.entity.UserWallet;
import com.example.test.global.time.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
// extends BaseTimeEntity -> createdAt, updatedAt 자동 생성
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 아이디
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;

    @Enumerated(EnumType.STRING)
    private Role role;

    // User 생성하면 UserWallet도 생성되어야 함
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private UserWallet userWallet;

    @Builder
    public User(String username, String email, String password, String firstName, String lastName) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = Role.ROLE_USER;
    }

    public void updateProfile(UpdateUserDto updateUserDto){
        if(updateUserDto.username() != null) this.username = updateUserDto.username();
        if(updateUserDto.firstName() != null) this.firstName = updateUserDto.firstName();
        if(updateUserDto.lastName() != null) this.lastName = updateUserDto.lastName();
    }

    public void deleteProfile() {
        this.role = Role.WITH_DRAW;
    }
}
