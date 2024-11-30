package com.scrum.parkingapp.exception;

public class RevokingTokenErrorException extends RuntimeException {
    public RevokingTokenErrorException(String message) {
        super(message);
    }
}
