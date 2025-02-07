package com.example.wms.infrastructure.repository;

import com.example.wms.user.application.domain.LogoutAccessToken;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class LogoutAccessTokenRedisRepository {

    private final RedisTemplate<String, LogoutAccessToken> redisTemplate;
    private static final String LOGOUT_ACCESS_TOKEN_KEY_PREFIX = "logout_access_token:";

    public LogoutAccessTokenRedisRepository(RedisTemplate<String, LogoutAccessToken> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // 저장
    public void save(LogoutAccessToken logoutAccessToken) {
        redisTemplate.opsForValue().set(LOGOUT_ACCESS_TOKEN_KEY_PREFIX + logoutAccessToken.getStaffNumber(), logoutAccessToken);
    }

    // 조회
    public Optional<LogoutAccessToken> findByStaffNumber(String staffNumber) {
        LogoutAccessToken logoutAccessToken = redisTemplate.opsForValue().get(LOGOUT_ACCESS_TOKEN_KEY_PREFIX + staffNumber);
        return Optional.ofNullable(logoutAccessToken);
    }

    public boolean existsByStaffNumber(String staffNumber) {
        return redisTemplate.hasKey(LOGOUT_ACCESS_TOKEN_KEY_PREFIX + staffNumber);
    }

    // 삭제
    public void deleteByStaffNumber(String staffNumber) {
        redisTemplate.delete(LOGOUT_ACCESS_TOKEN_KEY_PREFIX + staffNumber);
    }
}
