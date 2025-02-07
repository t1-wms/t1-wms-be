package com.example.wms.user.application.service;

import com.example.wms.user.adapter.in.dto.response.TokenInfo;
import com.example.wms.user.application.port.out.JwtTokenPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtTokenService {

    private final JwtTokenPort jwtTokenPort;
    private final RefreshTokenService refreshTokenService;

    public TokenInfo generateAndSaveTokens(String staffNumber) {
        // JWT 토큰 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(staffNumber, null, null);
        TokenInfo tokenInfo = jwtTokenPort.generateToken(authentication);

        // 리프레시 토큰 저장
        refreshTokenService.saveRefreshToken(staffNumber, tokenInfo.getRefreshToken());

        return tokenInfo;
    }

    public void invalidateAccessToken(String accessToken) {
        // 토큰 만료 처리
        log.info("[액세스 토큰 만료] 토큰 만료 처리: {}", accessToken);
    }

}
