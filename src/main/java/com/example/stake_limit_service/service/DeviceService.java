package com.example.stake_limit_service.service;

import com.example.stake_limit_service.dto.DeviceResponse;
import com.example.stake_limit_service.entity.Device;
import com.example.stake_limit_service.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository repository;
    public List<DeviceResponse> createDevices(Integer number) {
        List<Device> devices = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            devices.add(new Device());
        }
        repository.saveAll(devices);

        return devices.stream()
                .map(device -> DeviceResponse.builder()
                        .id(device.getId())
                        .blocked(device.isBlocked())
                        .restrictionExpires(device.isRestrictionExpires())
                        .restrictionExpiresAt(device.getRestrictionExpiresAt())
                        .build())
                .toList();
    }

    public List<DeviceResponse> getDevices() {
        return repository.findAll().stream()
                .map(device -> DeviceResponse.builder()
                        .id(device.getId())
                        .blocked(device.isBlocked())
                        .restrictionExpires(device.isRestrictionExpires())
                        .restrictionExpiresAt(device.getRestrictionExpiresAt())
                        .build())
                .toList();
    }
}
