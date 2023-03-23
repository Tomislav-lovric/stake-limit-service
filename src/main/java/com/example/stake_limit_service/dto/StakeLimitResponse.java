package com.example.stake_limit_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StakeLimitResponse {
    private Integer timeDuration;
    private Double stakeLimit;
    private Integer hotAmountPctg;
    private Integer restrExpiry;
}
