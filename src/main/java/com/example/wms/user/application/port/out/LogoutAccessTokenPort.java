package com.example.wms.user.application.port.out;

import com.example.wms.user.application.domain.LogoutAccessToken;

public interface LogoutAccessTokenPort {
    void save(LogoutAccessToken logoutAccessToken);  // 로그아웃한 토큰 저장
}
