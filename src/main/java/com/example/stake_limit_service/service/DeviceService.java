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
    //simple method for creating devices in db
    public String createDevices(Integer number) {
        List<Device> devices = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            devices.add(new Device());
        }
        repository.saveAll(devices);
        return number + " devices created";
    }

    //simple method for getting all devices in our db
    //needed for getting deviceId which is used in all other services
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
