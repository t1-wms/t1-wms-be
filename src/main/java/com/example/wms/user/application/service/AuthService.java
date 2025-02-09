package com.example.wms.user.application.service;

import com.example.wms.infrastructure.exception.DuplicatedException;
import com.example.wms.infrastructure.repository.LogoutAccessTokenRedisRepository;
import com.example.wms.user.adapter.in.dto.request.LoginReqDto;
import com.example.wms.user.adapter.in.dto.request.SignUpReqDto;
import com.example.wms.user.adapter.in.dto.response.AuthenticatedResDto;
import com.example.wms.user.adapter.in.dto.response.TokenInfo;
import com.example.wms.user.adapter.in.dto.response.UserInfoResDto;
import com.example.wms.user.application.domain.LogoutAccessToken;
import com.example.wms.user.application.domain.User;
import com.example.wms.user.application.domain.enums.UserExceptionMessage;
import com.example.wms.user.application.exception.UserNotFoundException;
import com.example.wms.user.application.port.in.AuthUseCase;
import com.example.wms.user.application.port.out.AuthPort;
import com.example.wms.user.application.port.out.UserQueryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final LogoutAccessTokenRedisRepository logoutAccessTokenRedisRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public AuthenticatedResDto signUp(SignUpReqDto signUpReqDto) {
        if (userQueryPort.existsByStaffNumber(signUpReqDto.getStaffNumber())) {
            throw new DuplicatedException(UserExceptionMessage.SIGN_UP_NOT_VALID.getMessage());
        }

        // 패스워드 암호화
        signUpReqDto.setPassword(passwordEncoder.encode(signUpReqDto.getPassword()));
        log.info("[회원가입] 패스워드 암호화 완료.");

        User user = authPort.save(signUpReqDto.dtoToEntity());

        TokenInfo tokenInfo = jwtTokenService.generateAndSaveTokens(user.getStaffNumber(), user.getPassword());

        log.info("[회원가입 성공] 사번: {}", user.getStaffNumber());

        return AuthenticatedResDto.builder()
                .userInfo(UserInfoResDto.entityToResDto(user))
                .tokenInfo(tokenInfo)
                .build();
    }

    @Override
    public AuthenticatedResDto login(LoginReqDto loginReqDto) {
        TokenInfo tokenInfo = jwtTokenService.generateAndSaveTokens(loginReqDto.getStaffNumber(), loginReqDto.getPassword());

        log.info("[로그인 성공] 사번: {}", loginReqDto.getStaffNumber());

        User user = userQueryPort.findByStaffNumber(loginReqDto.getStaffNumber())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND.getMessage()));
        logoutAccessTokenRedisRepository.deleteByStaffNumber(loginReqDto.getStaffNumber());
        refreshTokenService.saveRefreshToken(user.getStaffNumber(), tokenInfo.getRefreshToken());

        return AuthenticatedResDto.builder()
                .userInfo(UserInfoResDto.entityToResDto(user))
                .tokenInfo(tokenInfo)
                .build();
    }

    @Override
    public void logout(String accessToken) {
        String staffNumber = getLoginUserStaffNumber();

        long remainMilliSeconds = jwtTokenService.getRemainingExpiration(accessToken);
        jwtTokenService.invalidateAccessToken(accessToken);
        refreshTokenService.deleteByStaffNumber(staffNumber);
        logoutAccessTokenRedisRepository.save(LogoutAccessToken.builder()
                .staffNumber(staffNumber)
                .accessToken(accessToken)
                .expiration(remainMilliSeconds)
                .build());

        log.info("[로그아웃] 로그아웃 완료.");
    }

    @Override
    public TokenInfo reissueToken(String refreshToken) {
        String staffNumber = refreshTokenService.validateAndGetStaffNumber(refreshToken);

        TokenInfo newTokenInfo = jwtTokenService.generateAndSaveTokens(staffNumber, null);

        log.info("[토큰 재발급 성공] 새로운 토큰 발급: {}", newTokenInfo.getAccessToken());

        refreshTokenService.saveRefreshToken(staffNumber, newTokenInfo.getRefreshToken());
        return newTokenInfo;
    }
}
