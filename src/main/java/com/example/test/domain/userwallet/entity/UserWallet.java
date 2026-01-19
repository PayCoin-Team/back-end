package com.example.test.domain.userwallet.entity;

import com.example.test.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserWallet {

    @Id
    @Column(name = "user_id")
    private Long id;

    // User와 1:1 관계
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(precision = 18, scale = 6)
    private BigDecimal balance;
    private String publicAddress;

    @Builder
    public UserWallet(User user, String publicAddress) {

        this.user = user;
        this.balance = BigDecimal.ZERO;
        this.publicAddress = publicAddress;
    }
}
