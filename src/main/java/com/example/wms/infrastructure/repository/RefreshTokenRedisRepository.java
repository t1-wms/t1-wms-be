package com.example.wms.infrastructure.repository;

import com.example.wms.user.application.domain.RefreshToken;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class RefreshTokenRedisRepository {

    private final RedisTemplate<String, RefreshToken> redisTemplate;
    private static final String REFRESH_TOKEN_KEY_PREFIX = "refresh_token:";

    public RefreshTokenRedisRepository(RedisTemplate<String, RefreshToken> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void save(RefreshToken refreshToken) {
        redisTemplate.opsForValue().set(REFRESH_TOKEN_KEY_PREFIX + refreshToken.getStaffNumber(), refreshToken);
    }

    public Optional<RefreshToken> findByStaffNumber(String staffNumber) {
        RefreshToken refreshToken = redisTemplate.opsForValue().get(REFRESH_TOKEN_KEY_PREFIX + staffNumber);
        return Optional.ofNullable(refreshToken);
    }

    public void deleteByStaffNumber(String staffNumber) {
        redisTemplate.delete(REFRESH_TOKEN_KEY_PREFIX + staffNumber);
    }
}
