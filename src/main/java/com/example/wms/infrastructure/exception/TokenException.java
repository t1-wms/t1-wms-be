package com.example.wms.infrastructure.exception;

import org.springframework.security.core.AuthenticationException;

public class TokenException extends AuthenticationException {
    public TokenException(String message) {
        super(message);
    }
}
