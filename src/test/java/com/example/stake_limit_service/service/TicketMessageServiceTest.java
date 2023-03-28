package com.example.stake_limit_service.service;

import com.example.stake_limit_service.dto.TicketMessageRequest;
import com.example.stake_limit_service.dto.TicketMessageStatusResponse;
import com.example.stake_limit_service.entity.Device;
import com.example.stake_limit_service.entity.StakeLimit;
import com.example.stake_limit_service.entity.TicketMessage;
import com.example.stake_limit_service.exception.DeviceNotFoundException;
import com.example.stake_limit_service.exception.StakeLimitNotFoundException;
import com.example.stake_limit_service.exception.TicketAlreadyExistsException;
import com.example.stake_limit_service.repository.DeviceRepository;
import com.example.stake_limit_service.repository.StakeLimitRepository;
import com.example.stake_limit_service.repository.TicketMessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketMessageServiceTest {

    @Mock
    private TicketMessageRepository ticketMessageRepository;

    @Mock
    private StakeLimitRepository stakeLimitRepository;

    @Mock
    private DeviceRepository deviceRepository;

    @InjectMocks
    private TicketMessageService ticketMessageService;

    private UUID ticketUuid;
    private Device device;
    private StakeLimit stakeLimit;
    private TicketMessage ticketMessage;
    private TicketMessageRequest ticketMessageRequest;

    @BeforeEach
    void setUp() {

        ticketUuid = UUID.randomUUID();
        device = Device.builder().id(UUID.fromString("799de2ee-13c2-40a1-8230-d7318de97925")).build();
        stakeLimit = StakeLimit.builder()
                .device(device)
                .timeDuration(1800)
                .stakeLimit(999.0)
                .hotAmountPctg(80)
                .restrExpiry(300)
                .build();
        ticketMessage = TicketMessage.builder()
                .id(ticketUuid)
                .device(device)
                .stake(100.0)
                .createdAt(LocalDateTime.now())
                .build();
        ticketMessageRequest = TicketMessageRequest.builder()
                .id(ticketUuid.toString())
                .deviceId(device.getId().toString())
                .stake(100.0)
                .build();
    }

    @Test
    void testCreateTicketShouldReturnTicketMessageStatusResponse() {
        // given

        // when
        when(ticketMessageRepository.existsById(ticketUuid)).thenReturn(false);
        when(deviceRepository.existsById(device.getId())).thenReturn(true);
        when(stakeLimitRepository.existsByDevice(device)).thenReturn(true);
        when(deviceRepository.findDeviceById(device.getId())).thenReturn(device);
        when(stakeLimitRepository.findByDevice(device)).thenReturn(stakeLimit);
        when(deviceRepository.save(any(Device.class))).thenReturn(device);
        when(ticketMessageRepository.save(any(TicketMessage.class))).thenReturn(ticketMessage);

        TicketMessageStatusResponse response = ticketMessageService.createTicket(ticketMessageRequest);

        // then
        assertThat(response).isNotNull();

    }

    @Test
    void testCreateTicketShouldThrowTicketAlreadyExistsException() {
        // given

        // when
        when(ticketMessageRepository.existsById(ticketUuid)).thenReturn(true);

        // then
        assertThatThrownBy(() -> ticketMessageService.createTicket(ticketMessageRequest))
                .isInstanceOf(TicketAlreadyExistsException.class)
                .hasMessageContaining("Ticket with " + ticketUuid + " already exists");
    }

    @Test
    void testCreateTicketShouldThrowDeviceNotFoundException() {
        // given

        // when
        when(ticketMessageRepository.existsById(ticketUuid)).thenReturn(false);
        when(deviceRepository.existsById(device.getId())).thenReturn(false);

        // then
        assertThatThrownBy(() -> ticketMessageService.createTicket(ticketMessageRequest))
                .isInstanceOf(DeviceNotFoundException.class)
                .hasMessageContaining("Device with id " + device.getId() + " not found");
    }

    @Test
    void testCreateTicketShouldThrowStakeLimitNotFoundException() {
        // given

        // when
        when(ticketMessageRepository.existsById(ticketUuid)).thenReturn(false);
        when(deviceRepository.existsById(device.getId())).thenReturn(true);
        when(deviceRepository.findDeviceById(device.getId())).thenReturn(device);
        when(stakeLimitRepository.existsByDevice(device)).thenReturn(false);

        // then
        assertThatThrownBy(() -> ticketMessageService.createTicket(ticketMessageRequest))
                .isInstanceOf(StakeLimitNotFoundException.class)
                .hasMessageContaining("Stake limit for device with id " + device.getId() + " not found");
    }
}