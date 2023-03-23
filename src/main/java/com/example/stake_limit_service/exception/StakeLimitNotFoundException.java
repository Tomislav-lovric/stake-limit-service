package com.example.stake_limit_service.exception;

public class StakeLimitNotFoundException extends RuntimeException {
    private String message;

    public StakeLimitNotFoundException() {
    }

    public StakeLimitNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}
