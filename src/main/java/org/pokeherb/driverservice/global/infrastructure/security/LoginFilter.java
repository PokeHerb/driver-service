package org.pokeherb.driverservice.global.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.util.UUID;

/**
 * 게이트웨이 넘어온 유저 정보 SecurityContextHolder에서 필터 처리
 */

@Component
public class LoginFilter extends GenericFilterBean {

    private static final String HEADER_USER_ID = "X-User-Id";
    private static final String HEADER_USERNAME = "X-Username";
    private static final String HEADER_ROLES = "X-User-Roles";
    private static final String HEADER_EMAIL = "X-User-Email";
    private static final String HEADER_USER_NAME = "X-User-Name";
    private static final String HEADER_HUB_ID = "X-Hub-Id";
    private static final String HEADER_VENDOR_ID = "X-Vendor-Id";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        doLogin((HttpServletRequest) servletRequest);
        filterChain.doFilter(servletRequest, servletResponse);

    }

    private void doLogin(HttpServletRequest servletRequest) {

        String userId = servletRequest.getHeader(HEADER_USER_ID);
        String username = servletRequest.getHeader(HEADER_USERNAME);
        String roles = servletRequest.getHeader(HEADER_ROLES);
        String email = servletRequest.getHeader(HEADER_EMAIL);
        String name = servletRequest.getHeader(HEADER_USER_NAME);
        String hubId = servletRequest.getHeader(HEADER_HUB_ID);
        String vendorId = servletRequest.getHeader(HEADER_VENDOR_ID);

        name = name != null ? URLDecoder.decode(name, StandardCharsets.UTF_8) : null;

        if (!StringUtils.hasText(userId) || !StringUtils.hasText(username)) {
            return;
        }


        CustomUserDetails userDetails = CustomUserDetails.builder()
                .userId(UUID.fromString(userId))
                .username(username)
                .email(email)
                .name(name)
                .roles(roles)
                .hubId(Long.parseLong(hubId))
                .vendorId(UUID.fromString(vendorId))
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
