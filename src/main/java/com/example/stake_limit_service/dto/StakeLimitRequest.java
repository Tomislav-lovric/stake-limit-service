package com.example.stake_limit_service.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StakeLimitRequest {
    //Using String instead of UUID because I could not get my custom validation to work
    @NotEmpty(message = "DeviceId field can't be empty")
    private String deviceId;

    @Min(value = 300, message = "Minimum time duration is 5 minutes (300 seconds)")
    @Max(value = 86400, message = "Maximum time duration is 24 hours (86400 seconds)")
    private Integer timeDuration;

    @Min(value = 1, message = "Minimum stake limit is 1")
    @Max(value = 10000000, message = "Maximum stake limit is 10000000")
    private Double stakeLimit;

    @Min(value = 1, message = "Minimum hot amount percentage is 1")
    @Max(value = 100, message = "Maximum hot amount percentage is 100")
    private Integer hotAmountPctg;

    @Min(value = 0, message = "Minimum restriction expiry is 1 minute (60 seconds)" +
            " while maximum is 0 seconds (it never expires)")
    private Integer restrExpiry;
}
