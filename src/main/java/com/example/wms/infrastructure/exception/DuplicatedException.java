package com.example.wms.infrastructure.exception;

public class DuplicatedException extends IllegalArgumentException {
    public DuplicatedException(String message) {
        super(message);
    }
}
