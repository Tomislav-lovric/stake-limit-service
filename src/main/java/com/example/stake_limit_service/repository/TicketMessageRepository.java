package com.example.stake_limit_service.repository;

import com.example.stake_limit_service.entity.Device;
import com.example.stake_limit_service.entity.TicketMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface TicketMessageRepository extends JpaRepository<TicketMessage, UUID> {

    List<TicketMessage> findByDeviceAndCreatedAtBetween(
            Device device,
            LocalDateTime createdAtStart,
            LocalDateTime createdAtEnd
    );
}
