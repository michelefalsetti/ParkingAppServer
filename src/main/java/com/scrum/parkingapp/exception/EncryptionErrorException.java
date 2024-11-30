package com.scrum.parkingapp.exception;

public class EncryptionErrorException extends RuntimeException {
    public EncryptionErrorException(String message) {
        super(message);
    }
}
