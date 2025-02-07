package com.example.wms.user.application.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@Getter
@RedisHash("refreshToken")
public class RefreshToken {

    @Id
    private String staffNumber;

    private String refreshToken;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private long expiration;

    @Builder
    public RefreshToken(String staffNumber, String refreshToken, long expiration) {
        this.staffNumber = staffNumber;
        this.refreshToken = refreshToken;
        this.expiration = expiration;
    }
}
