package com.app.skhuaz.jwt.service;

import com.app.skhuaz.exception.ErrorCode;
import com.app.skhuaz.exception.exceptions.NotValidTokenException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenManager tokenManager;

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 토큰 정보 가져오기
        String token = resolveToken(request);

        if (StringUtils.hasText(token) && tokenManager.validateToken(token)) { // 토큰이 존재하고 유효한 경우
            Claims claims = tokenManager.getTokenClaims(token);

            if (claims != null) {
                String email = tokenManager.getMemberEmail(token);
                String tokenType = tokenManager.getTokenType(token);


                // 인증 정보 저장
                Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, null);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            // 만료된 경우
            if (tokenManager.isTokenExpired(claims.getExpiration())) {
                throw new NotValidTokenException(ErrorCode.NOT_VALID_TOKEN);
            }
        }

        // 다음 필터로 전달
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}