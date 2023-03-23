package com.example.stake_limit_service.exception;

public class UuidNotValidException extends RuntimeException {

    private String message;

    public UuidNotValidException() {
    }

    public UuidNotValidException(String message) {
        super(message);
        this.message = message;
    }
}
