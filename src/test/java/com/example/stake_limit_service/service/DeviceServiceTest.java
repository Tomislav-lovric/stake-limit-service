package com.example.stake_limit_service.service;

import com.example.stake_limit_service.dto.DeviceResponse;
import com.example.stake_limit_service.entity.Device;
import com.example.stake_limit_service.repository.DeviceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeviceServiceTest {

    @Mock
    private DeviceRepository deviceRepository;

    @InjectMocks
    private DeviceService deviceService;

    @Test
    void testCreateDevicesShouldReturnDeviceResponse() {
        // given
        int number = 3;
        List<Device> devices = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            devices.add(new Device());
        }

        // when
        when(deviceRepository.saveAll(Mockito.anyList())).thenReturn(devices);

        // then
        List<DeviceResponse> savedDevices = deviceService.createDevices(number);

        assertThat(savedDevices).hasSizeGreaterThan(0);
        verify(deviceRepository).saveAll(anyList());
    }

    @Test
    void testGetDevicesShouldReturnDeviceResponse() {
        // when
        when(deviceRepository.findAll()).thenReturn(Arrays.asList(new Device(), new Device()));

        List<DeviceResponse> deviceList = deviceService.getDevices();

        // then
        assertThat(deviceList).hasSizeGreaterThan(0);
        verify(deviceRepository).findAll();
    }
}