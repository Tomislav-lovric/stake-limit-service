package com.example.stake_limit_service.controller;

import com.example.stake_limit_service.dto.TicketMessageRequest;
import com.example.stake_limit_service.dto.TicketMessageStatusResponse;
import com.example.stake_limit_service.entity.Device;
import com.example.stake_limit_service.service.TicketMessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

@WebMvcTest(TicketMessageController.class)
class TicketMessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TicketMessageService ticketMessageService;

    private static final String END_POINT_PATH = "/api/v1/ticket-message/create-ticket";

    @Test
    void createTicket() throws Exception {
        // given
        UUID ticketUuid = UUID.randomUUID();
        Device device = Device.builder().id(UUID.fromString("799de2ee-13c2-40a1-8230-d7318de97925")).build();
        TicketMessageRequest ticketMessageRequest = TicketMessageRequest.builder()
                .id(ticketUuid.toString())
                .deviceId(device.getId().toString())
                .stake(100.0)
                .build();
        TicketMessageStatusResponse ticketStatus = TicketMessageStatusResponse.builder().status("OK").build();

        // when
        when(ticketMessageService.createTicket(ticketMessageRequest)).thenReturn(ticketStatus);

        // then
        mockMvc.perform(post(END_POINT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ticketMessageRequest)))
                .andExpect(status().isCreated());

    }
}