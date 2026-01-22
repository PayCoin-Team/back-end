package com.example.test.domain.transaction.entity;

import com.example.test.domain.transaction.enums.Status;
import com.example.test.domain.transaction.enums.Type;
import com.example.test.global.time.BaseCreateEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.example.test.domain.externalWallet.entity.ExternalWallet;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "transaction")
@NoArgsConstructor
public class Transaction extends BaseCreateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    private String txId;

    @Column(name = "log_index")
    private Integer logIndex;

    @NotNull
    @Column(name = "type", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private Type type;

    @NotNull
    @Column(name = "status", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private Status status;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "externalwallet", nullable = false)
    private ExternalWallet externalWallet;

    @Builder
    public Transaction(BigDecimal amount, String toAddress, String fromAddress, String txId, Type type, ExternalWallet externalWallet) {

        this.amount = amount;
        this.toAddress = toAddress;
        this.fromAddress = fromAddress;
        this.txId = txId;
        this.logIndex = null;
        this.type = type;
        this.status = Status.PENDING;
        this.externalWallet = externalWallet;
    }

}