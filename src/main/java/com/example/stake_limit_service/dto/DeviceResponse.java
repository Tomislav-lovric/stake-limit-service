package com.example.stake_limit_service.dto;

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
public class DeviceResponse {
    private UUID id;
    private boolean blocked;
    private boolean restrictionExpires;
    private LocalDateTime restrictionExpiresAt;
}
