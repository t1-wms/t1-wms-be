package com.example.wms.user.application.service;

import com.example.wms.infrastructure.jwt.enums.JwtHeaderUtil;
import com.example.wms.user.adapter.in.dto.response.UserInfoResDto;
import com.example.wms.user.application.domain.User;
import com.example.wms.user.application.port.in.UserUseCase;
import com.example.wms.user.application.port.out.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.example.wms.infrastructure.security.util.SecurityUtils.getLoginUserStaffNumber;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserUseCase {

    private final AuthPort authPort;
    private final UserQueryPort userQueryPort;
    private final JwtTokenPort jwtTokenPort;
    private final RefreshTokenPort refreshTokenPort;
    private final UserPort userPort;

    @Override
    public void deleteUser() {
        String staffNumber = getLoginUserStaffNumber();
        User user = userPort.findByStaffNumber(staffNumber);

        log.debug("[회원 탈퇴] 탈퇴 요청. 로그인 유저 : {}", user.getStaffNumber());

        refreshTokenPort.deleteByStaffNumber(staffNumber);
        userPort.deleteUser(staffNumber);
    }

    @Override
    public UserInfoResDto findUser() {
        String staffNumber = getLoginUserStaffNumber();

        return UserInfoResDto.entityToResDto(userPort.findByStaffNumber(staffNumber));
    }

    /**
     * Access Token에서 Bearer 타입을 제거한 순수 Token 값 반환
     */
    private String parseAccessToken(String accessToken) {
        return accessToken.substring(JwtHeaderUtil.GRANT_TYPE.getValue().length());
    }
}
