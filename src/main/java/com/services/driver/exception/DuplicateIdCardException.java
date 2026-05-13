package com.services.driver.exception;

public class DuplicateIdCardException extends RuntimeException {
    public DuplicateIdCardException(String message) {
        super(message);
    }
}