package com.services.driver.exception;

public class DuplicateLicenseCategoryException extends RuntimeException {
    public DuplicateLicenseCategoryException(String message) {
        super(message);
    }
}