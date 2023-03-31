package com.example.stake_limit_service.controller;

import com.example.stake_limit_service.dto.TicketMessageRequest;
import com.example.stake_limit_service.dto.TicketMessageStatusResponse;
import com.example.stake_limit_service.service.TicketMessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/ticket-message")
@RequiredArgsConstructor
public class TicketMessageController {

    private final TicketMessageService service;

    @PostMapping("/create-ticket")
    public ResponseEntity<TicketMessageStatusResponse> createTicket(
            @RequestBody @Valid TicketMessageRequest request
    ) {
//        return ResponseEntity.ok(service.createTicket(request));
        return new ResponseEntity<>(service.createTicket(request), HttpStatus.CREATED);
    }
}
