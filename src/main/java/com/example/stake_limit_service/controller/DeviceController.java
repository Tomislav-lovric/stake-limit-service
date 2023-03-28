package com.example.stake_limit_service.controller;

import com.example.stake_limit_service.dto.DeviceRequest;
import com.example.stake_limit_service.dto.DeviceResponse;
import com.example.stake_limit_service.service.DeviceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/device")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService service;

    //because devices aren't that important to project I just created 2 simple methods and endpoints
    //to create 1 or more devices as well to get all devices (this one mainly for their id, so it
    //can be used for testing other endpoints)
    @GetMapping
    public ResponseEntity<List<DeviceResponse>> getDevices() {
        return ResponseEntity.ok(service.getDevices());
    }

    @PostMapping("/create")
    public ResponseEntity<List<DeviceResponse>> createDevices(@RequestBody @Valid DeviceRequest request) {
        return ResponseEntity.ok(service.createDevices(request.getNumber()));
    }
}
