package com.scrum.parkingapp.exception;

public class IsTokenRevokedException extends RuntimeException {
    public IsTokenRevokedException(String message) {
        super(message);
    }
}

