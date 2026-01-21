package com.example.test.domain.transaction.entity;

import com.example.test.global.time.BaseCreateEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import com.example.test.domain.externalWallet.entity.ExternalWallet;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "transaction")
public class Transaction extends BaseCreateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "amount", nullable = false, precision = 18, scale = 6)
    private BigDecimal amount;

    @Size(max = 64)
    @NotNull
    @Column(name = "to_address", nullable = false, length = 64)
    private String toAddress;

    @Size(max = 64)
    @Column(name = "from_address", length = 64)
    private String fromAddress;

    @Size(max = 100)
    @Column(name = "txid", length = 100)
    private String txid;

    @Size(max = 30)
    @NotNull
    @Column(name = "type", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private Type type;

    @Size(max = 30)
    @NotNull
    @Column(name = "status", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private Status status;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "externalwallet", nullable = false)
    private ExternalWallet externalWallet;

}