package com.services.driver.exception;

public class SubstatusNotFoundException extends RuntimeException {
    public SubstatusNotFoundException(String message) {
        super(message);
    }
}