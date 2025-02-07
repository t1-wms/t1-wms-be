package com.example.wms.infrastructure.jwt.dto;

import com.example.wms.infrastructure.jwt.enums.TokenType;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class Token {
    private final TokenType tokenType;
    private final String token;
}
