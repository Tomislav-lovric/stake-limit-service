package com.example.stake_limit_service.exception;

public class DeviceAlreadyExistsException extends RuntimeException {
    private String message;

    public DeviceAlreadyExistsException() {
    }

    public DeviceAlreadyExistsException(String message) {
        super(message);
        this.message = message;
    }
}
