package com.example.wms.infrastructure.security;

import com.example.wms.infrastructure.security.domain.CustomUserDetails;
import com.example.wms.infrastructure.security.service.CustomUserDetailsService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final CustomUserDetailsService userDetailsService;

    public CustomAuthenticationProvider(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String staffNumber  = authentication.getName();

        // 사용자 로드 (비밀번호 체크 없이 수행)
        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(staffNumber );

        // 인증 성공 시 Authentication 객체 반환
        return new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
