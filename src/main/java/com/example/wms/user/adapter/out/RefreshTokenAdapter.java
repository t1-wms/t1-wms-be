package com.example.wms.user.adapter.out;

import com.example.wms.user.application.domain.RefreshToken;
import com.example.wms.user.application.port.out.RefreshTokenPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RefreshTokenAdapter implements RefreshTokenPort {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String REFRESH_TOKEN_KEY_PREFIX = "refresh_token:";

    @Override
    public void save(RefreshToken refreshToken) {
        // Redis에 RefreshToken 저장
        redisTemplate.opsForValue().set(REFRESH_TOKEN_KEY_PREFIX + refreshToken.getStaffNumber(), refreshToken);
    }

    @Override
    public Optional<RefreshToken> findByStaffNumber(String staffNumber) {
        // Redis에서 staffNumber를 기준으로 RefreshToken을 찾음
        RefreshToken refreshToken = (RefreshToken) redisTemplate.opsForValue().get(REFRESH_TOKEN_KEY_PREFIX + staffNumber);
        return Optional.ofNullable(refreshToken);
    }

    @Override
    public void deleteByStaffNumber(String staffNumber) {
        // Redis에서 staffNumber에 해당하는 RefreshToken 삭제
        redisTemplate.delete(REFRESH_TOKEN_KEY_PREFIX + staffNumber);
    }
}
