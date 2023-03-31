package com.example.stake_limit_service.controller;

import com.example.stake_limit_service.dto.StakeLimitRequest;
import com.example.stake_limit_service.dto.StakeLimitResponse;
import com.example.stake_limit_service.service.StakeLimitService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/stake-limit")
@RequiredArgsConstructor
@Validated
public class StakeLimitController {

    private final StakeLimitService service;

    //changed param to path var
    @GetMapping("/{deviceId}")
    public ResponseEntity<StakeLimitResponse> getStakeLimit(@PathVariable String deviceId) {
        return ResponseEntity.ok(service.getStakeLimit(deviceId));
    }

    //changed status from 200 ok to 201 created
    @PostMapping("/add-limit")
    public ResponseEntity<StakeLimitResponse> addStakeLimit(
            @RequestBody @Valid StakeLimitRequest request
    ) {
//        return ResponseEntity.ok(service.addStakeLimit(request));
        return new ResponseEntity<>(service.addStakeLimit(request), HttpStatus.CREATED);
    }

    //or use path variable
    @PutMapping("/change-limit")
    public ResponseEntity<StakeLimitResponse> changeStakeLimit(
            @RequestParam String deviceId,
            @RequestParam(required = false)
            @Min(value = 300, message = "Minimum time duration is 5 minutes (300 seconds)")
            @Max(value = 86400, message = "Maximum time duration is 24 hours (86400 seconds)")
            Integer timeDuration,
            @RequestParam(required = false)
            @Min(value = 1, message = "Minimum stake limit is 1")
            @Max(value = 10000000, message = "Maximum stake limit is 10000000")
            Double stakeLimit,
            @RequestParam(required = false)
            @Min(value = 1, message = "Minimum hot amount percentage is 1")
            @Max(value = 100, message = "Maximum hot amount percentage is 100")
            Integer hotAmountPctg,
            @RequestParam(required = false)
            @Min(value = 0, message = "Minimum restriction expiry is 1 minute (60 seconds)" +
                    " while maximum is 0 seconds (it never expires)")
            Integer restrExpiry
    ) {
        return ResponseEntity.ok(
                service.changeStakeLimit(deviceId, timeDuration, stakeLimit, hotAmountPctg, restrExpiry
                ));
    }

}
