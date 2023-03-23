package com.example.stake_limit_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Device")
@Table(name = "devices")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(
            name = "id",
            updatable = false
    )
    private UUID id;

    @Column(name = "blocked")
    private boolean blocked;

    @Column(name = "restriction_expires")
    private boolean restrictionExpires;

    @Column(name = "restriction_expires_at")
    private LocalDateTime restrictionExpiresAt;
}
