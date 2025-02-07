package com.example.wms.user.application.exception;

public class InvalidSignUpException extends RuntimeException {
    public InvalidSignUpException(String message) {
        super(message);
    }
}
