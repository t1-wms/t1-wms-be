package com.example.wms.infrastructure.jwt.exception;

public class NotMatchedTokenException extends RuntimeException {
    public NotMatchedTokenException(String message) {
        super(message);
    }
}
