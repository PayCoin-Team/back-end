package com.example.test.domain.polling.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "polling")
public class Polling {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "last_block", nullable = false)
    private Long lastBlock;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

}