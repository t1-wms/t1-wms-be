package com.example.wms.user.adapter.out;

import com.example.wms.infrastructure.jwt.JwtTokenProvider;
import com.example.wms.user.adapter.in.dto.response.TokenInfo;
import com.example.wms.user.application.port.out.JwtTokenPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenAdapter implements JwtTokenPort {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public TokenInfo generateToken(Authentication authentication) {
        return jwtTokenProvider.generateToken(authentication);
    }

    @Override
    public String getEmailFromToken(String token) {
        // 토큰에서 이메일을 추출하는 메서드
        return jwtTokenProvider.getEmailFromToken(token);
    }

    @Override
    public boolean validateToken(String token) {
        try {
            return jwtTokenProvider.validateToken(token);
        } catch (Exception e) {
            return false; // 유효하지 않은 토큰이라면 false 반환
        }
    }

    @Override
    public boolean validateRefreshToken(String refreshToken) {
        // Refresh Token의 유효성 검증
        return jwtTokenProvider.validateRefreshToken(refreshToken);
    }

    @Override
    public String getUsernameFromExpiredToken(String token) {
        // 만료된 토큰에서 사용자 이름을 추출
        return jwtTokenProvider.getUsernameFromExpiredToken(token);
    }

    @Override
    public long getRemainingExpiration(String token) {
        // 토큰의 남은 만료 시간 계산
        return jwtTokenProvider.getRemainingExpiration(token);
    }
}
