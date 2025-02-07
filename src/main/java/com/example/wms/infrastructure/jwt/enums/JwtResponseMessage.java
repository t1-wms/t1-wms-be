package com.example.wms.infrastructure.jwt.enums;

import lombok.Getter;

@Getter
public enum JwtResponseMessage {
    TOKEN_REISSUED("토큰 재발급 완료");

    private final String message;

    JwtResponseMessage(String message) {
        this.message = message;
    }
}
