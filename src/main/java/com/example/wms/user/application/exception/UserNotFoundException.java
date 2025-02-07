package com.example.wms.user.application.exception;

import com.example.wms.infrastructure.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
