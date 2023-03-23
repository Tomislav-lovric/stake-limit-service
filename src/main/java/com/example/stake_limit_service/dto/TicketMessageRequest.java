package com.example.stake_limit_service.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketMessageRequest {

    //Using String instead of UUID because I could not get my custom validation to work
    //also not using @NotEmpty for testing purposes (check ticket service for better explanation)
//    @NotEmpty(message = "Id field can't be empty")
    private String id;

    //Using String instead of UUID because I could not get my custom validation to work
    @NotEmpty(message = "DeviceId field can't be empty")
    private String deviceId;

    @Min(value = 1, message = "Minimum stake is 1")
    @Max(value = 10000000, message = "Maximum stake is 10000000")
    private Double stake;

}
