package com.example.test.domain.history.entity;

import com.example.test.domain.userwallet.entity.UserWallet;
import com.example.test.global.time.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class History extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(precision = 18, scale = 6)
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "send_wallet_id")
    private UserWallet sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receive_wallet_id")
    private UserWallet receiver;

    @Builder
    public History(BigDecimal amount, UserWallet sender, UserWallet receiver) {

        this.amount = amount;
        this.sender = sender;
        this.receiver = receiver;
    }
}
