package com.example.stake_limit_service.exception;

public class DeviceNotFoundException extends RuntimeException {
    private String message;

    public DeviceNotFoundException() {
    }

    public DeviceNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}
