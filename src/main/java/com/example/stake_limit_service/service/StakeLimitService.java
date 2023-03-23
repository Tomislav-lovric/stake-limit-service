package com.example.stake_limit_service.service;

import com.example.stake_limit_service.dto.StakeLimitRequest;
import com.example.stake_limit_service.dto.StakeLimitResponse;
import com.example.stake_limit_service.entity.StakeLimit;
import com.example.stake_limit_service.exception.DeviceAlreadyExistsException;
import com.example.stake_limit_service.exception.DeviceNotFoundException;
import com.example.stake_limit_service.exception.StakeLimitNotFoundException;
import com.example.stake_limit_service.exception.UuidNotValidException;
import com.example.stake_limit_service.repository.DeviceRepository;
import com.example.stake_limit_service.repository.StakeLimitRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StakeLimitService {

    private final StakeLimitRepository stakeLimitRepository;
    private final DeviceRepository deviceRepository;

    public StakeLimitResponse getStakeLimit(String deviceId) {
        UUID deviceUuid = isUuidValid(deviceId);
        if (!deviceRepository.existsById(deviceUuid)) {
            throw new DeviceNotFoundException("Device with " + deviceUuid + " id not found");
        }
        if (!stakeLimitRepository.existsByDevice(deviceRepository.findDeviceById(deviceUuid))) {
            throw new StakeLimitNotFoundException("Stake limit with device " + deviceUuid + " id not found");
        }

        var device = deviceRepository.findDeviceById(deviceUuid);
        var stakeLimit = stakeLimitRepository.findByDevice(device);
        return StakeLimitResponse.builder()
                .timeDuration(stakeLimit.getTimeDuration())
                .stakeLimit(stakeLimit.getStakeLimit())
                .hotAmountPctg(stakeLimit.getHotAmountPctg())
                .restrExpiry(stakeLimit.getRestrExpiry())
                .build();
    }

    public StakeLimitResponse addStakeLimit(StakeLimitRequest request) {
        UUID deviceId = isUuidValid(request.getDeviceId());
        //Could also make it if deviceId does not exist, create device with that id
        if (!deviceRepository.existsById(deviceId)) {
            throw new DeviceNotFoundException("Device with " + deviceId + " id not found");
        }
        if (stakeLimitRepository.existsByDevice(deviceRepository.findDeviceById(deviceId))) {
            throw new DeviceAlreadyExistsException(
                    "Stake limit with device " + deviceId + " id already exists"
            );
        }
        var device = deviceRepository.findDeviceById(deviceId);

        device.setRestrictionExpires(request.getRestrExpiry() != 0);

        var stakeLimit = StakeLimit.builder()
                .device(device)
                .timeDuration(request.getTimeDuration())
                .stakeLimit(request.getStakeLimit())
                .hotAmountPctg(request.getHotAmountPctg())
                .restrExpiry(request.getRestrExpiry())
                .build();
        stakeLimitRepository.save(stakeLimit);

        return StakeLimitResponse.builder()
                .timeDuration(stakeLimit.getTimeDuration())
                .stakeLimit(stakeLimit.getStakeLimit())
                .hotAmountPctg(stakeLimit.getHotAmountPctg())
                .restrExpiry(stakeLimit.getRestrExpiry())
                .build();
    }

    @Transactional
    public StakeLimitResponse changeStakeLimit(
            String deviceId,
            Integer timeDuration,
            Double stakeLimit,
            Integer hotAmountPctg,
            Integer restrExpiry
    ) {
        UUID deviceUuid = isUuidValid(deviceId);
        if (!deviceRepository.existsById(deviceUuid)) {
            throw new DeviceNotFoundException("Device with " + deviceUuid + " id not found");
        }
        if (!stakeLimitRepository.existsByDevice(deviceRepository.findDeviceById(deviceUuid))) {
            throw new StakeLimitNotFoundException("Stake limit for device with id " + deviceUuid + " not found");
        }
        var device = deviceRepository.findDeviceById(deviceUuid);
        var stakeLimitDB = stakeLimitRepository.findByDevice(device);

        if (timeDuration != null && !timeDuration.equals(stakeLimitDB.getTimeDuration())) {
            stakeLimitDB.setTimeDuration(timeDuration);
        }
        if (stakeLimit != null && !stakeLimit.equals(stakeLimitDB.getStakeLimit())) {
            stakeLimitDB.setStakeLimit(stakeLimit);
        }
        if (hotAmountPctg != null && !hotAmountPctg.equals(stakeLimitDB.getHotAmountPctg())) {
            stakeLimitDB.setHotAmountPctg(hotAmountPctg);
        }
        if (restrExpiry != null && !restrExpiry.equals(stakeLimitDB.getRestrExpiry())) {
            device.setRestrictionExpires(restrExpiry != 0);
            deviceRepository.save(device);
            stakeLimitDB.setRestrExpiry(restrExpiry);
        }
        stakeLimitRepository.save(stakeLimitDB);

        return StakeLimitResponse.builder()
                .timeDuration(stakeLimitDB.getTimeDuration())
                .stakeLimit(stakeLimitDB.getStakeLimit())
                .hotAmountPctg(stakeLimitDB.getHotAmountPctg())
                .restrExpiry(stakeLimitDB.getRestrExpiry())
                .build();
    }

    private UUID isUuidValid(String uuid) {
        final String uuid_pattern = "^[0-9A-Fa-f]{8}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{12}$";

        if (!uuid.matches(uuid_pattern)) {
            throw new UuidNotValidException(uuid + " UUID not valid");
        }

        return UUID.fromString(uuid);
    }
}
