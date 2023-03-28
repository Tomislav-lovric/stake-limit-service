package com.example.stake_limit_service.service;

import com.example.stake_limit_service.dto.StakeLimitRequest;
import com.example.stake_limit_service.dto.StakeLimitResponse;
import com.example.stake_limit_service.entity.Device;
import com.example.stake_limit_service.entity.StakeLimit;
import com.example.stake_limit_service.exception.DeviceAlreadyExistsException;
import com.example.stake_limit_service.exception.DeviceNotFoundException;
import com.example.stake_limit_service.exception.StakeLimitNotFoundException;
import com.example.stake_limit_service.repository.DeviceRepository;
import com.example.stake_limit_service.repository.StakeLimitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StakeLimitServiceTest {

    @Mock
    private StakeLimitRepository stakeLimitRepository;

    @Mock
    private DeviceRepository deviceRepository;

    @InjectMocks
    private StakeLimitService stakeLimitService;

    private StakeLimit stakeLimit;
    private StakeLimitRequest stakeLimitRequest;
    private Device device;

    @BeforeEach
    void setUp() {
        device = Device.builder().id(UUID.fromString("799de2ee-13c2-40a1-8230-d7318de97925")).build();
        stakeLimit = StakeLimit.builder()
                .device(device)
                .timeDuration(1800)
                .stakeLimit(999.0)
                .hotAmountPctg(80)
                .restrExpiry(300)
                .build();
        stakeLimitRequest = StakeLimitRequest.builder()
                .deviceId(device.getId().toString())
                .timeDuration(1800)
                .stakeLimit(999.0)
                .hotAmountPctg(80)
                .restrExpiry(300)
                .build();
    }

    @Test
    void testGetStakeLimitShouldReturnStakeLimitResponse() {
        // given

        // when
        when(deviceRepository.existsById(device.getId())).thenReturn(true);
        when(deviceRepository.findDeviceById(device.getId())).thenReturn(device);
        when(stakeLimitRepository.existsByDevice(device)).thenReturn(true);
        when(stakeLimitRepository.findByDevice(device)).thenReturn(stakeLimit);

        StakeLimitResponse response = stakeLimitService.getStakeLimit(device.getId().toString());

        // then
        assertThat(response).isNotNull();
        assertThat(response.getTimeDuration()).isEqualTo(stakeLimit.getTimeDuration());
        assertThat(response.getStakeLimit()).isEqualTo(stakeLimit.getStakeLimit());
        assertThat(response.getHotAmountPctg()).isEqualTo(stakeLimit.getHotAmountPctg());
        assertThat(response.getRestrExpiry()).isEqualTo(stakeLimit.getRestrExpiry());
    }

    @Test
    void testGetStakeLimitShouldThrowDeviceNotFoundException() {
        // given

        // when
        when(deviceRepository.existsById(device.getId())).thenReturn(false);

        // then
        assertThatThrownBy(() -> stakeLimitService.getStakeLimit(device.getId().toString()))
                .isInstanceOf(DeviceNotFoundException.class)
                .hasMessageContaining("Device with " + device.getId() + " id not found");
    }

    @Test
    void testGetStakeLimitShouldThrowStakeLimitNotFoundException() {
        // given

        // when
        when(deviceRepository.existsById(device.getId())).thenReturn(true);
        when(deviceRepository.findDeviceById(device.getId())).thenReturn(device);
        when(stakeLimitRepository.existsByDevice(device)).thenReturn(false);

        // then
        assertThatThrownBy(() -> stakeLimitService.getStakeLimit(device.getId().toString()))
                .isInstanceOf(StakeLimitNotFoundException.class)
                .hasMessageContaining("Stake limit with device " + device.getId() + " id not found");
    }

    @Test
    void testAddStakeLimitShouldReturnStakeLimitResponse() {
        // given

        // when
        when(deviceRepository.existsById(device.getId())).thenReturn(true);
        when(deviceRepository.findDeviceById(device.getId())).thenReturn(device);
        when(stakeLimitRepository.existsByDevice(device)).thenReturn(false);
        when(stakeLimitRepository.save(any(StakeLimit.class))).thenReturn(stakeLimit);

        StakeLimitResponse savedStakeLimit = stakeLimitService.addStakeLimit(stakeLimitRequest);

        // then
        assertThat(savedStakeLimit).isNotNull();
        assertThat(savedStakeLimit.getTimeDuration()).isEqualTo(stakeLimit.getTimeDuration());
        assertThat(savedStakeLimit.getStakeLimit()).isEqualTo(stakeLimit.getStakeLimit());
        assertThat(savedStakeLimit.getHotAmountPctg()).isEqualTo(stakeLimit.getHotAmountPctg());
        assertThat(savedStakeLimit.getRestrExpiry()).isEqualTo(stakeLimit.getRestrExpiry());
    }

    @Test
    void testAddStakeLimitShouldThrowDeviceNotFoundException() {
        // given

        // when
        when(deviceRepository.existsById(device.getId())).thenReturn(false);

        // then
        assertThatThrownBy(() -> stakeLimitService.addStakeLimit(stakeLimitRequest))
                .isInstanceOf(DeviceNotFoundException.class)
                .hasMessageContaining("Device with " + device.getId() + " id not found");
    }

    @Test
    void testAddStakeLimitShouldThrowDeviceAlreadyExistsException() {
        // given

        // when
        when(deviceRepository.existsById(device.getId())).thenReturn(true);
        when(deviceRepository.findDeviceById(device.getId())).thenReturn(device);
        when(stakeLimitRepository.existsByDevice(device)).thenReturn(true);

        // then
        assertThatThrownBy(() -> stakeLimitService.addStakeLimit(stakeLimitRequest))
                .isInstanceOf(DeviceAlreadyExistsException.class)
                .hasMessageContaining("Stake limit with device " + device.getId() + " id already exists");
    }

    @Test
    void testChangeStakeLimitShouldReturnStakeLimitResponse() {
        // given
        int timeDur = 1801;
        double stakeLim = 1000.0;
        int hotAmntPerc = 81;
        int restExpiry = 601;
        stakeLimit.setTimeDuration(timeDur);
        stakeLimit.setStakeLimit(stakeLim);
        stakeLimit.setHotAmountPctg(hotAmntPerc);
        stakeLimit.setRestrExpiry(restExpiry);

        // when
        when(deviceRepository.existsById(device.getId())).thenReturn(true);
        when(stakeLimitRepository.existsByDevice(device)).thenReturn(true);
        when(deviceRepository.findDeviceById(device.getId())).thenReturn(device);
        when(stakeLimitRepository.findByDevice(device)).thenReturn(stakeLimit);
        when(stakeLimitRepository.save(any(StakeLimit.class))).thenReturn(stakeLimit);

        StakeLimitResponse changedStakeLimit = stakeLimitService.changeStakeLimit(
                device.getId().toString(),
                timeDur,
                stakeLim,
                hotAmntPerc,
                restExpiry
        );

        // then
        assertThat(changedStakeLimit).isNotNull();
        assertThat(changedStakeLimit.getTimeDuration()).isEqualTo(timeDur);
        assertThat(changedStakeLimit.getStakeLimit()).isEqualTo(stakeLim);
        assertThat(changedStakeLimit.getHotAmountPctg()).isEqualTo(hotAmntPerc);
        assertThat(changedStakeLimit.getRestrExpiry()).isEqualTo(restExpiry);

    }

    @Test
    void testChangeStakeLimitShouldThrowDeviceNotFoundException() {
        // give
        int timeDur = 1801;
        double stakeLim = 1000.0;
        int hotAmntPerc = 81;
        int restExpiry = 601;

        // when
        when(deviceRepository.existsById(device.getId())).thenReturn(false);

        // then
        assertThatThrownBy(() -> stakeLimitService.changeStakeLimit(
                device.getId().toString(),
                timeDur,
                stakeLim,
                hotAmntPerc,
                restExpiry
        ))
                .isInstanceOf(DeviceNotFoundException.class)
                .hasMessageContaining("Device with " + device.getId() + " id not found");
    }

    @Test
    void testChangeStakeLimitShouldThrowStakeLimitNotFoundException() {
        // give
        int timeDur = 1801;
        double stakeLim = 1000.0;
        int hotAmntPerc = 81;
        int restExpiry = 601;

        // when
        when(deviceRepository.existsById(device.getId())).thenReturn(true);
        when(deviceRepository.findDeviceById(device.getId())).thenReturn(device);
        when(stakeLimitRepository.existsByDevice(device)).thenReturn(false);

        // then
        assertThatThrownBy(() -> stakeLimitService.changeStakeLimit(
                device.getId().toString(),
                timeDur,
                stakeLim,
                hotAmntPerc,
                restExpiry
        ))
                .isInstanceOf(StakeLimitNotFoundException.class)
                .hasMessageContaining("Stake limit for device with id " + device.getId() + " not found");
    }

}