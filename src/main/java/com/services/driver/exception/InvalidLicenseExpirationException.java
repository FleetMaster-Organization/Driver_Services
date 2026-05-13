package com.services.driver.exception;

public class InvalidLicenseExpirationException extends RuntimeException {
    public InvalidLicenseExpirationException(String message) {
        super(message);
    }
}