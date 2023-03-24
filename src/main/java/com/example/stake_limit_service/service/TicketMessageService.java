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
        //check if UUID is valid
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
        //after that is done check if ticket with that id already exists
        if (ticketMessageRepository.existsById(ticketUuid)) {
            //if it does throw exp
            throw new TicketAlreadyExistsException("Ticket with " + ticketUuid + " already exists");
        }
        //then check if device with provided deviceId exists
        if (!deviceRepository.existsById(deviceUuid)) {
            //if it does not throw exp
            throw new DeviceNotFoundException("Device with id " + deviceUuid + " not found");
        }
        //then check if that device has defined stake limits
        if (!stakeLimitRepository.existsByDevice(deviceRepository.findDeviceById(deviceUuid))) {
            //if it does not throw exp
            throw new StakeLimitNotFoundException(
                    "Stake limit for device with id " + deviceUuid + " not found"
            );
        }

        //if all checks have been passed create current datetime
        LocalDateTime now = LocalDateTime.now();
        //get device
        var device = deviceRepository.findDeviceById(deviceUuid);

        //check if device is blocked and restriction does not expire or
        // check if device is blocked and restriction has not expired
        if ((device.isBlocked() && !device.isRestrictionExpires()) ||
                (device.isBlocked() && device.getRestrictionExpiresAt().isAfter(now))) {
            //if one of those (multi) conditions is true build and return our response
            return TicketMessageStatusResponse.builder().status("BLOCKED").build();
        }
        //else if device is not blocked or is blocked but restriction expired
        //set blocked in device to false
        device.setBlocked(false);

        //then get our stake limits for that device
        var stakeLimit = stakeLimitRepository.findByDevice(device);
        //get all tickets by that device in period between now and now minus time limit
        //provided in our stake limit for that device
        List<TicketMessage> tickets = ticketMessageRepository.findByDeviceAndCreatedAtBetween(
                device,
                now.minus(stakeLimit.getTimeDuration(), ChronoUnit.SECONDS),
                now
        );
        //then sum all stakes from those tickets
        double sumOfStakes = tickets.stream().mapToDouble(TicketMessage::getStake).sum();

        //and check if that sum surpasses our allowed stake limit that was set
        if ((sumOfStakes + request.getStake()) >= stakeLimit.getStakeLimit()) {
            //if it does set blocked in our device to true
            device.setBlocked(true);
            //then check if restriction expiry in our stake limit is set to 0
            //(which means restriction does never expire)
            if (stakeLimit.getRestrExpiry() == 0) {
                //set restriction expires in our device to false
                device.setRestrictionExpires(false);
                //and save our device
                deviceRepository.save(device);
            } else {
                //if restriction expiry is not 0 set restr expires at to now plus restr expiry
                device.setRestrictionExpiresAt(now.plus(stakeLimit.getRestrExpiry(), ChronoUnit.SECONDS));
                //and save our device
                deviceRepository.save(device);
            }
            //we don't build ticket here because it goes over allowed stake limit
            //we just block user and return msg with status BLOCKED
            return TicketMessageStatusResponse.builder().status("BLOCKED").build();
        }

        //could/should probably build ticketMessage above here and then just save it either in if block
        //or outside of it (depending on if condition is true or not) to reduce redundancy in code

        //if above check is not true then we check if our sum surpasses hot amount percentage
        //set in our stake limit
        if ((sumOfStakes + request.getStake()) >=
                ((double) stakeLimit.getHotAmountPctg() / 100) * stakeLimit.getStakeLimit()) {
            //if it does save our device then build our ticket and save it to db
            deviceRepository.save(device);
            var ticketMessage = TicketMessage.builder()
                    .id(ticketUuid)
                    .device(device)
                    .stake(request.getStake())
                    .createdAt(now)
                    .build();
            ticketMessageRepository.save(ticketMessage);
            //and finally build and return our response
            return TicketMessageStatusResponse.builder().status("HOT").build();
        }

        //if none of the two above our true save our device and build and save our ticket
        deviceRepository.save(device);
        var ticketMessage = TicketMessage.builder()
                .id(ticketUuid)
                .device(device)
                .stake(request.getStake())
                .createdAt(now)
                .build();
        ticketMessageRepository.save(ticketMessage);
        //and finally build and return our response
        return TicketMessageStatusResponse.builder().status("OK").build();
    }

    //method used for checking if provided UUID is correct
    //because I could not get custom validation to work properly I switched all requests
    //to take String instead of pure UUID, and then i check if that String matches
    //UUID pattern, if it does not match pattern throw custom exception else
    //transform provided String into actual UUID
    private UUID isUuidValid(String uuid) {
        final String uuid_pattern = "^[0-9A-Fa-f]{8}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{12}$";

        if (!uuid.matches(uuid_pattern)) {
            throw new UuidNotValidException(uuid + " UUID not valid");
        }

        return UUID.fromString(uuid);
    }
}
