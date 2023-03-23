package com.example.stake_limit_service.exception;

public class TicketAlreadyExistsException extends RuntimeException {
    private String message;

    public TicketAlreadyExistsException() {
    }

    public TicketAlreadyExistsException(String message) {
        super(message);
        this.message = message;
    }
}
