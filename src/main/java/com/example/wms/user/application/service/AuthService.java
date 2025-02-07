package com.example.wms.user.application.service;

import com.example.wms.user.adapter.in.dto.request.LoginReqDto;
import com.example.wms.user.adapter.in.dto.request.SignUpReqDto;
import com.example.wms.user.adapter.in.dto.response.AuthenticatedResDto;
import com.example.wms.user.adapter.in.dto.response.TokenInfo;
import com.example.wms.user.adapter.in.dto.response.UserInfoResDto;
import com.example.wms.user.application.domain.User;
import com.example.wms.user.application.domain.enums.UserExceptionMessage;
import com.example.wms.user.application.exception.InvalidSignUpException;
import com.example.wms.user.application.exception.UserNotFoundException;
import com.example.wms.user.application.port.in.AuthUseCase;
import com.example.wms.user.application.port.out.AuthPort;
import com.example.wms.user.application.port.out.UserQueryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.example.wms.infrastructure.security.util.SecurityUtils.getLoginUserStaffNumber;
import static com.example.wms.user.application.domain.enums.UserExceptionMessage.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService implements AuthUseCase {

    private final AuthPort authPort;
    private final UserQueryPort userQueryPort;
    private final JwtTokenService jwtTokenService;
    private final RefreshTokenService refreshTokenService;

    @Override
    public AuthenticatedResDto signUp(SignUpReqDto signUpReqDto) {
        if (userQueryPort.existsByStaffNumber(signUpReqDto.getStaffNumber())) {
            throw new InvalidSignUpException(UserExceptionMessage.SIGN_UP_NOT_VALID.getMessage());
        }

        User user = authPort.save(signUpReqDto.dtoToEntity());

        TokenInfo tokenInfo = jwtTokenService.generateAndSaveTokens(user.getStaffNumber());

        log.info("[회원가입 성공] 이메일: {}", user.getStaffNumber());

        return AuthenticatedResDto.builder()
                .userInfo(UserInfoResDto.entityToResDto(user))
                .tokenInfo(tokenInfo)
                .build();
    }

    @Override
    public AuthenticatedResDto login(LoginReqDto loginReqDto) {
        User user = userQueryPort.findByStaffNumber(loginReqDto.getStaffNumber())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND.getMessage()));

        TokenInfo tokenInfo = jwtTokenService.generateAndSaveTokens(user.getStaffNumber());

        log.info("[로그인 성공] 이메일: {}", user.getStaffNumber());

        return AuthenticatedResDto.builder()
                .userInfo(UserInfoResDto.entityToResDto(user))
                .tokenInfo(tokenInfo)
                .build();
    }

    @Override
    public void logout(String accessToken) {
        String email = getLoginUserStaffNumber();
        jwtTokenService.invalidateAccessToken(accessToken);

        refreshTokenService.deleteByEmail(email);

        log.info("[로그아웃] 로그아웃 완료.");
    }

    @Override
    public TokenInfo reissueToken(String refreshToken) {
        String email = refreshTokenService.validateAndGetEmail(refreshToken);

        TokenInfo newTokenInfo = jwtTokenService.generateAndSaveTokens(email);

        log.info("[토큰 재발급 성공] 새로운 토큰 발급: {}", newTokenInfo.getAccessToken());

        return newTokenInfo;
    }
}
