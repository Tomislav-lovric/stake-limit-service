package com.example.stake_limit_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "StakeLimit")
@Table(name = "stake_limits")
public class StakeLimit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(
            name = "id",
            updatable = false
    )
    private UUID id;

    @ManyToOne
    @JoinColumn(
            name = "device_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "device_id_fk"
            )
    )
    private Device device;

    @Column(
            name = "time_duration",
            nullable = false
    )
    private Integer timeDuration;

    @Column(
            name = "stake_limit",
            nullable = false
    )
    private Double stakeLimit;

    @Column(
            name = "hot_amount_pctg",
            nullable = false
    )
    private Integer hotAmountPctg;

    @Column(
            name = "restr_expiry",
            nullable = false
    )
    private Integer restrExpiry;
}
