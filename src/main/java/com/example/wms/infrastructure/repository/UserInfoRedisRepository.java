package com.example.wms.infrastructure.repository;

import com.example.wms.user.application.domain.UserInfo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserInfoRedisRepository {

    private final RedisTemplate<String, UserInfo> redisTemplate;
    private static final String USER_INFO_KEY_PREFIX = "user_info:";

    public UserInfoRedisRepository(RedisTemplate<String, UserInfo> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Optional<UserInfo> findByStaffNumber(String staffNumber) {
        UserInfo userInfo = redisTemplate.opsForValue().get(USER_INFO_KEY_PREFIX + staffNumber);
        return Optional.ofNullable(userInfo);
    }
}
