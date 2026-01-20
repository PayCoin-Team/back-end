package com.example.test.domain.polling.model;

import com.example.test.global.time.BaseUpdatedEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "polling")
public class Polling extends BaseUpdatedEntity {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "last_timestamp", nullable = false)
    private Long lastTimestamp;

}