package com.example.wms.user.application.port.out;

import com.example.wms.user.application.domain.RefreshToken;
import java.util.Optional;

public interface RefreshTokenPort {
    void save(RefreshToken refreshToken);  // 리프레시 토큰 저장
    Optional<RefreshToken> findByStaffNumber(String staffNumber);  // 사번으로 리프레시 토큰 조회
    void deleteByStaffNumber(String staffNumber);  // 사번으로 리프레시 토큰 삭제
}
