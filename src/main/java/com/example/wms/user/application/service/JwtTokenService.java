package com.example.wms.user.application.service;

import com.example.wms.user.adapter.in.dto.response.TokenInfo;
import com.example.wms.user.application.port.in.JwtTokenUseCase;
import com.example.wms.user.application.port.out.JwtTokenPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtTokenService implements JwtTokenUseCase {

    private final JwtTokenPort jwtTokenPort;
    private final AuthenticationManager authenticationManager;

    @Override
    public TokenInfo generateAndSaveTokens(String staffNumber, String password) {
        // 인증 처리
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(staffNumber, password);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        // 토큰 생성 및 반환
        return jwtTokenPort.generateToken(authentication);
    }

    @Override
    public void invalidateAccessToken(String accessToken) {
        log.info("[액세스 토큰 만료] 토큰 만료 처리: {}", accessToken);
    }

    @Override
    public String getStaffNumberFromToken(String token) {
        // 토큰에서 staffNumber 추출
        return jwtTokenPort.getStaffNumberFromToken(token);
    }

    @Override
    public long getRemainingExpiration(String token) {
        // 남은 토큰 만료 시간 계산
        return jwtTokenPort.getRemainingExpiration(token);
    }
}
