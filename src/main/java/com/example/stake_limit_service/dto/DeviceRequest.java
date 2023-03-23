package com.example.stake_limit_service.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceRequest {
    @Min(value = 1, message = "Number of devices you want to create can't be lower than 1")
    @Max(value = 1000, message = "Number of devices you want to create can't be higher than 1000")
    private Integer number;
}
