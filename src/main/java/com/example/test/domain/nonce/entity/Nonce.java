package com.example.test.domain.nonce.entity;

import com.example.test.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Nonce {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nonce; // 식별 문자

    @Column(nullable = false)
    private String walletAddress; // 연동 시도 중인 지갑 주소

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt; // 생성 시간

    @Column(nullable = false)
    private LocalDateTime expiredAt; // 만료 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 요청한 유저

    @Builder
    public Nonce(String nonce, String walletAddress, User user, int expiryMinutes) {
        this.nonce = nonce;
        this.walletAddress = walletAddress;
        this.user = user;
        this.createdAt = LocalDateTime.now();
        this.expiredAt = this.createdAt.plusMinutes(expiryMinutes);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiredAt);
    }
}
