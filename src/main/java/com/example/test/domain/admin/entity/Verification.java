package com.example.test.domain.admin.entity;

import com.example.test.global.time.BaseCreateEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "verification")
public class Verification extends BaseCreateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "server_balance", nullable = false, precision = 18, scale = 6)
    private BigDecimal serverBalance;

    @NotNull
    @Column(name = "user_balance", nullable = false, precision = 18, scale = 6)
    private BigDecimal userBalance;

    @NotNull
    @Column(name = "total_fee", nullable = false, precision = 18, scale = 6)
    private BigDecimal totalFee;

    @Size(max = 15)
    @NotNull
    @Column(name = "difference", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private Difference difference;

}