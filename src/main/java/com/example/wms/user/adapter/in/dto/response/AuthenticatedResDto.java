package com.example.wms.user.adapter.in.dto.response;

import com.example.wms.user.application.domain.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticatedResDto {
    private final TokenInfo tokenInfo;
    private final UserInfoResDto userInfo;

    public static AuthenticatedResDto entityToResDto(TokenInfo tokenInfo, User user) {
        UserInfoResDto userInfo = UserInfoResDto.entityToResDto(user);
        return AuthenticatedResDto.builder()
                .tokenInfo(tokenInfo)
                .userInfo(userInfo)
                .build();
    }
}