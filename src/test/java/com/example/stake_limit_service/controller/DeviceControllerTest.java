package com.example.stake_limit_service.controller;

import com.example.stake_limit_service.dto.DeviceRequest;
import com.example.stake_limit_service.dto.DeviceResponse;
import com.example.stake_limit_service.entity.Device;
import com.example.stake_limit_service.service.DeviceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DeviceController.class)
class DeviceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DeviceService deviceService;

    private static final String END_POINT_PATH = "/api/v1/device";
    private DeviceResponse deviceResponse1;
    private DeviceResponse deviceResponse2;

    @BeforeEach
    void setUp() {
        Device device1 = new Device();
        Device device2 = new Device();
        deviceResponse1 = DeviceResponse.builder()
                .id(device1.getId())
                .blocked(device1.isBlocked())
                .restrictionExpires(device1.isRestrictionExpires())
                .restrictionExpiresAt(device1.getRestrictionExpiresAt())
                .build();
        deviceResponse2 = DeviceResponse.builder()
                .id(device2.getId())
                .blocked(device2.isBlocked())
                .restrictionExpires(device2.isRestrictionExpires())
                .restrictionExpiresAt(device2.getRestrictionExpiresAt())
                .build();
    }

    @Test
    void testGetDevicesShouldReturnOk() throws Exception {
        // given
        List<DeviceResponse> devices = Arrays.asList(deviceResponse1, deviceResponse2);

        // when
        when(deviceService.getDevices()).thenReturn(devices);

        // then
        mockMvc.perform(get(END_POINT_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(devices.get(0).getId()));

    }

    @Test
    void testCreateDevicesShouldReturnCreated() throws Exception {
        // given
        List<DeviceResponse> devices = Arrays.asList(deviceResponse1, deviceResponse2);
        DeviceRequest deviceRequest = DeviceRequest.builder().number(3).build();

        // when
        when(deviceService.createDevices(deviceRequest.getNumber())).thenReturn(devices);

        // then
        mockMvc.perform(post(END_POINT_PATH + "/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(deviceRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].id").value(devices.get(0).getId()));
    }
}