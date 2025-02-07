package com.example.wms.infrastructure.security.util;

import com.example.wms.infrastructure.mapper.UserMapper;
import com.example.wms.user.application.domain.enums.UserExceptionMessage;
import com.example.wms.user.application.domain.User;
import com.example.wms.user.application.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.example.wms.infrastructure.enums.ExceptionMessage.NOT_FOUND_LOGIN_USER;


@Component
@RequiredArgsConstructor
public class SecurityUtils {
    private final UserMapper userMapper;

    public static String getLoginUserStaffNumber() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            throw new IllegalStateException(NOT_FOUND_LOGIN_USER.getMessage());
        }

        return authentication.getName();
    }

    public User getLoginUser() {
        try {
            Authentication authentication = Objects.requireNonNull(SecurityContextHolder
                    .getContext()
                    .getAuthentication());

            if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
                throw new IllegalStateException(NOT_FOUND_LOGIN_USER.getMessage());
            }

            return userMapper.findByStaffNumber(authentication.getName())
                    .orElseThrow(() -> new UserNotFoundException(UserExceptionMessage.USER_NOT_FOUND.getMessage()));
        } catch (NullPointerException e) {
            throw new RuntimeException(e);
        }
    }
}
