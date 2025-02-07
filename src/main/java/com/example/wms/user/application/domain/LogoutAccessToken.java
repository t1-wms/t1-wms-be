package com.example.wms.user.application.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@RedisHash("logoutAccessToken")
public class LogoutAccessToken {

    @Id
    private String staffNumber;

    private String accessToken;

    @TimeToLive
    private long expiration;

    @Builder
    public LogoutAccessToken(String staffNumber, String accessToken, long expiration) {
        this.staffNumber = staffNumber;
        this.accessToken = accessToken;
        this.expiration = expiration;
    }
}
