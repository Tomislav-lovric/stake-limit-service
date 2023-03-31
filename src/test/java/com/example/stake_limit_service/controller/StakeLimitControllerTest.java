package com.example.stake_limit_service.controller;

import com.example.stake_limit_service.dto.StakeLimitRequest;
import com.example.stake_limit_service.dto.StakeLimitResponse;
import com.example.stake_limit_service.entity.Device;
import com.example.stake_limit_service.service.StakeLimitService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StakeLimitController.class)
class StakeLimitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StakeLimitService stakeLimitService;

    private static final String END_POINT_PATH = "/api/v1/stake-limit";
    private StakeLimitRequest stakeLimitRequest;
    private StakeLimitResponse stakeLimitResponse;
    private Device device;

    @BeforeEach
    void setUp() {
        device = Device.builder().id(UUID.fromString("799de2ee-13c2-40a1-8230-d7318de97925")).build();
        stakeLimitRequest = StakeLimitRequest.builder()
                .deviceId(device.getId().toString())
                .timeDuration(1800)
                .stakeLimit(999.0)
                .hotAmountPctg(80)
                .restrExpiry(300)
                .build();
        stakeLimitResponse = StakeLimitResponse.builder()
                .timeDuration(1800)
                .stakeLimit(999.0)
                .hotAmountPctg(80)
                .restrExpiry(300)
                .build();
    }

    @Test
    void getStakeLimit() throws Exception {
        // given
        UUID id = device.getId();

        // when
        when(stakeLimitService.getStakeLimit(id.toString())).thenReturn(stakeLimitResponse);

        // then
        mockMvc.perform(get(END_POINT_PATH + "/{deviceId}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.timeDuration").value(stakeLimitResponse.getTimeDuration()))
                .andExpect(jsonPath("$.stakeLimit").value(stakeLimitResponse.getStakeLimit()))
                .andExpect(jsonPath("$.hotAmountPctg").value(stakeLimitResponse.getHotAmountPctg()))
                .andExpect(jsonPath("$.restrExpiry").value(stakeLimitResponse.getRestrExpiry()));

    }

    @Test
    void addStakeLimit() throws Exception {
        // when
        when(stakeLimitService.addStakeLimit(stakeLimitRequest)).thenReturn(stakeLimitResponse);

        // then
        mockMvc.perform(post(END_POINT_PATH + "/add-limit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(stakeLimitRequest)))
                .andExpect(status().isCreated());

    }

    @Test
    void changeStakeLimit() throws Exception {
        // given
        int timeDur = 1801;
        double stakeLim = 1000.0;
        int hotAmntPerc = 81;
        int restExpiry = 601;
        String deviceId = device.getId().toString();

        // when
        when(stakeLimitService.changeStakeLimit(
                deviceId,
                timeDur,
                stakeLim,
                hotAmntPerc,
                restExpiry
        )).thenReturn(stakeLimitResponse);

        // then
        mockMvc.perform(put(END_POINT_PATH + "/change-limit")
                .contentType(MediaType.APPLICATION_JSON)
                .param("deviceId", deviceId)
                .param("timeDuration", String.valueOf(timeDur))
                .param("stakeLimit", String.valueOf(stakeLim))
                .param("hotAmountPctg", String.valueOf(hotAmntPerc))
                .param("restrExpiry", String.valueOf(restExpiry)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.timeDuration").value(stakeLimitResponse.getTimeDuration()))
                .andExpect(jsonPath("$.stakeLimit").value(stakeLimitResponse.getStakeLimit()))
                .andExpect(jsonPath("$.hotAmountPctg").value(stakeLimitResponse.getHotAmountPctg()))
                .andExpect(jsonPath("$.restrExpiry").value(stakeLimitResponse.getRestrExpiry()));

    }
}