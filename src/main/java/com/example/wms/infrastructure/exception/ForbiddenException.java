package com.example.wms.infrastructure.exception;

public class ForbiddenException extends SecurityException {
    public ForbiddenException(String message) {
        super(message);
    }
}
