package com.example.wms.infrastructure.jwt.exception;

public class MalformedHeaderException extends RuntimeException {
    public MalformedHeaderException(String message) {
        super(message);
    }
}
