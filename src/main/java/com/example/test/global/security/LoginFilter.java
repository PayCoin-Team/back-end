package com.example.test.global.security;

import com.example.test.global.security.jwt.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper; // JSON 파싱용 라이브러리
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    public static final long ACCESS_TOKEN_EXPIRED_TIME = 1 * 60 * 60 * 1000L;
    // public static final long REFRESH_TOKEN_EXPIRED_TIME = 1 * 24 * 60 * 60 * 1000L; (구현 보류)

    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 변환기 생성
    private final JwtUtil jwtUtil;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            LoginDto loginDto = objectMapper.readValue(request.getInputStream(), LoginDto.class);

            String username = loginDto.getUsername();
            String password = loginDto.getPassword();

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(username, password, null);

            return authenticationManager.authenticate(authToken);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Getter @Setter
    private static class LoginDto {
        private String username;
        private String password;
    }

    // 로그인 성공 시 실행
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {

        CustomUserDetails customUserDetails = (CustomUserDetails)  authentication.getPrincipal();

        Long userId = customUserDetails.getId();
        String username = customUserDetails.getUsername();

        // role
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String token = jwtUtil.createJwt(userId, username, role, ACCESS_TOKEN_EXPIRED_TIME);

        response.addHeader("Authorization", "Bearer " + token);

    }

    // 로그인 실패 시 실행
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {

        response.setStatus(401);
    }
}