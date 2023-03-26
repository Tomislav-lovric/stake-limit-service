package com.example.stake_limit_service.repository;

import com.example.stake_limit_service.entity.Device;
import com.example.stake_limit_service.entity.TicketMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class TicketMessageRepositoryTest {

    @Autowired
    private TicketMessageRepository ticketMessageRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @AfterEach
    void tearDown() {
        ticketMessageRepository.deleteAll();
    }

    @Test
    void TicketMessageRepository_findByDeviceAndCreatedAtBetween_ReturnTicketMessage() {
        // given
        Device device = new Device();
        deviceRepository.save(device);

        TicketMessage ticketMessage1 = TicketMessage.builder()
                .id(UUID.fromString("799de2ee-13c2-40a1-8230-d7318de97925"))
                .device(device)
                .stake(100.0)
                .createdAt(LocalDateTime.now())
                .build();

        TicketMessage ticketMessage2 = TicketMessage.builder()
                .id(UUID.fromString("899de2ee-13c2-40a1-8230-d7318de97925"))
                .device(device)
                .stake(200.0)
                .createdAt(LocalDateTime.now().minusSeconds(60))
                .build();

        ticketMessageRepository.save(ticketMessage1);
        ticketMessageRepository.save(ticketMessage2);

        // when
        List<TicketMessage> ticketMessageList = ticketMessageRepository.findByDeviceAndCreatedAtBetween(
                device,
                LocalDateTime.now().minusSeconds(100),
                LocalDateTime.now()
        );

        // then
        assertThat(ticketMessageList).hasSize(2);
    }
}