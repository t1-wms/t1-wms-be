package com.example.wms.user.application.domain;

import com.example.wms.user.application.domain.enums.UserRole;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@RedisHash(value = "user_info")
public class UserInfo {

    @Id
    private String userId;
    private String staffNumber;
    private String name;
    private UserRole userRole;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private long expiration;
}
