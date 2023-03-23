package com.example.stake_limit_service.service;

import com.example.stake_limit_service.dto.TicketMessageRequest;
import com.example.stake_limit_service.dto.TicketMessageStatusResponse;
import com.example.stake_limit_service.entity.TicketMessage;
import com.example.stake_limit_service.exception.DeviceNotFoundException;
import com.example.stake_limit_service.exception.StakeLimitNotFoundException;
import com.example.stake_limit_service.exception.TicketAlreadyExistsException;
import com.example.stake_limit_service.exception.UuidNotValidException;
import com.example.stake_limit_service.repository.DeviceRepository;
import com.example.stake_limit_service.repository.StakeLimitRepository;
import com.example.stake_limit_service.repository.TicketMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketMessageService {

    private final TicketMessageRepository ticketMessageRepository;
    private final StakeLimitRepository stakeLimitRepository;
    private final DeviceRepository deviceRepository;

    public TicketMessageStatusResponse createTicket(TicketMessageRequest request) {
        UUID ticketUuid;
        UUID deviceUuid = isUuidValid(request.getDeviceId());

        //while the task requires us to send ticket id in the body always and therefore this part
        //is wrong/not needed, I simply added it to make testing the code easier, because with
        //this part added I don't need to constantly change ticket id when I'm testing creating the tickets
        if (request.getId() == null) {
            ticketUuid = UUID.randomUUID();
        } else {
            ticketUuid = isUuidValid(request.getId());
        }

        if (ticketMessageRepository.existsById(ticketUuid)) {
            throw new TicketAlreadyExistsException("Ticket with " + ticketUuid + " already exists");
        }
        if (!deviceRepository.existsById(deviceUuid)) {
            throw new DeviceNotFoundException("Device with id " + deviceUuid + " not found");
        }
        if (!stakeLimitRepository.existsByDevice(deviceRepository.findDeviceById(deviceUuid))) {
            throw new StakeLimitNotFoundException(
                    "Stake limit for device with id " + deviceUuid + " not found"
            );
        }

        LocalDateTime now = LocalDateTime.now();
        var device = deviceRepository.findDeviceById(deviceUuid);

        if (device.isBlocked() && device.getRestrictionExpiresAt().isAfter(now)) {
            return TicketMessageStatusResponse.builder().status("BLOCKED").build();
        }
        device.setBlocked(false);

        var stakeLimit = stakeLimitRepository.findByDevice(device);
        List<TicketMessage> tickets = ticketMessageRepository.findByDeviceAndCreatedAtBetween(
                device,
                now.minus(stakeLimit.getTimeDuration(), ChronoUnit.SECONDS),
                now
        );
        double sumOfStakes = tickets.stream().mapToDouble(TicketMessage::getStake).sum();

        if ((sumOfStakes + request.getStake()) >= stakeLimit.getStakeLimit()) {
            device.setBlocked(true);
            device.setRestrictionExpiresAt(now.plus(stakeLimit.getRestrExpiry(), ChronoUnit.SECONDS));
            deviceRepository.save(device);
            //we don't build ticket here because it goes over allowed stake limit
            //we just block user and return msg with status BLOCKED
            return TicketMessageStatusResponse.builder().status("BLOCKED").build();
        }

        //could/should probably build ticketMessage above here and then just save it either in if block
        //or outside of it (depending on if condition is true or not) to reduce redundancy in code
        if ((sumOfStakes + request.getStake()) >= ((double) stakeLimit.getHotAmountPctg() / 100) * stakeLimit.getStakeLimit()) {
            deviceRepository.save(device);
            var ticketMessage = TicketMessage.builder()
                    .id(ticketUuid)
                    .device(device)
                    .stake(request.getStake())
                    .createdAt(now)
                    .build();
            ticketMessageRepository.save(ticketMessage);
            return TicketMessageStatusResponse.builder().status("HOT").build();
        }

        deviceRepository.save(device);
        var ticketMessage = TicketMessage.builder()
                .id(ticketUuid)
                .device(device)
                .stake(request.getStake())
                .createdAt(now)
                .build();
        ticketMessageRepository.save(ticketMessage);
        return TicketMessageStatusResponse.builder().status("OK").build();
    }

    private UUID isUuidValid(String uuid) {
        final String uuid_pattern = "^[0-9A-Fa-f]{8}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{12}$";

        if (!uuid.matches(uuid_pattern)) {
            throw new UuidNotValidException(uuid + " UUID not valid");
        }

        return UUID.fromString(uuid);
    }
}
