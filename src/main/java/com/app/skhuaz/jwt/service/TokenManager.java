package com.app.skhuaz.jwt.service;

import com.app.skhuaz.exception.ErrorCode;
import com.app.skhuaz.exception.exceptions.NotValidTokenException;
import com.app.skhuaz.jwt.constant.GrantType;
import com.app.skhuaz.jwt.constant.TokenType;
import com.app.skhuaz.jwt.dto.TokenDto;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
@Slf4j
@Component
public class TokenManager {

    @Value("${token.access-token-expiration-time}")
    private String accessTokenExpirationTime;

    @Value("${token.refresh-token-expiration-time}")
    private String refreshTokenExpirationTime;

    @Value("${token.secret}")
    private String tokenSecret;


    public TokenDto createTokenDto(String email) {
        Date accessTokenExpireTime = createAccessTokenExpireTime();
        Date refreshTokenExpireTime = createRefreshTokenExpireTime();

        String accessToken = createAccessToken(email, accessTokenExpireTime);
        String refreshToken = createRefreshToken(email, refreshTokenExpireTime);
        return TokenDto.builder()
                .grantType(GrantType.BEARER.getType())
                .accessToken(accessToken)
                .accessTokenExpireTime(accessTokenExpireTime)
                .refreshToken(refreshToken)
                .refreshTokenExpireTime(refreshTokenExpireTime)
                .build();
    }

    private Date createAccessTokenExpireTime() {
        // return new Date(System.currentTimeMillis() + Long.parseLong(accessTokenExpirationTime));
        long expirationMillis = Long.parseLong(accessTokenExpirationTime);
        return new Date(System.currentTimeMillis() + expirationMillis);
    }

    private Date createRefreshTokenExpireTime() {
        // return new Date(System.currentTimeMillis() + Long.parseLong(refreshTokenExpirationTime));
        long expirationMillis = Long.parseLong(refreshTokenExpirationTime);
        return new Date(System.currentTimeMillis() + expirationMillis);
    }

    private String createAccessToken(String email, Date expirationTime) {
        String accessToken = Jwts.builder()
                .setSubject(TokenType.ACCESS.name())                // 토큰 제목
                .setAudience(email)                                 // 토큰 대상자
                .setIssuedAt(new Date())                            // 토큰 발급 시간
                .setExpiration(expirationTime)                      // 토큰 만료 시간
                /**
                 *      Claim 에는 key-value 로 여러 값 저장 가능한 듯
                 *      이렇게 토큰 내부에 정보를 저장해두면
                 *      DB에서 정보를 조회해 Role을 확인하거나 할 필요가 없음. 토큰만 뜯어도 정보가 있어서.
                 */
//                .claim("role", role)                          // 유저 role.
                .signWith(SignatureAlgorithm.HS512, tokenSecret.getBytes())
                .setHeaderParam("typ", "JWT")
                .compact();
        return accessToken;
    }

    private String createRefreshToken(String email, Date expirationTime) {
        String refreshToken = Jwts.builder()
                .setSubject(TokenType.REFRESH.name())               // 토큰 제목
                .setAudience(email)                                 // 토큰 대상자
                .setIssuedAt(new Date())                            // 토큰 발급 시간
                .setExpiration(expirationTime)                      // 토큰 만료 시간
                .signWith(SignatureAlgorithm.HS512, tokenSecret.getBytes())
                .setHeaderParam("typ", "JWT")
                .compact();
        return refreshToken;
    }

    public String getMemberEmail(String accessToken) {
        String email;
        try {
            Claims claims = Jwts.parser().setSigningKey(tokenSecret.getBytes())
                    .parseClaimsJws(accessToken).getBody();
            email = claims.getAudience(); // 이메일을 'email' 클레임에서 가져옴
        } catch (Exception e){
            e.printStackTrace();
            throw new NotValidTokenException(ErrorCode.NOT_VALID_TOKEN);
        }
        return email;
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(tokenSecret.getBytes()).parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            log.info("잘못된 jwt token", e);
            // 유효하지 않은 토큰 처리 로직 추가
        } catch (JwtException e) {
            log.info("jwt token 검증 중 에러 발생", e);
            // 검증 중에 발생한 다른 예외 처리 로직 추가
        }
        return false;
    }

    public Claims getTokenClaims(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(tokenSecret.getBytes())  // 수정된 부분
                    .parseClaimsJws(token).getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw new NotValidTokenException(ErrorCode.NOT_VALID_TOKEN);
        }
        return claims;
    }


    public boolean isTokenExpired(Date tokenExpiredTime) {
        Date now = new Date();
        if(now.after(tokenExpiredTime)) { //토큰 만료된 경우
            return true;
        }
        return false;
    }

    public String getTokenType(String token){
        String tokenType;
        try{
            Claims claims = Jwts.parser().setSigningKey(tokenSecret.getBytes())
                    .parseClaimsJws(token).getBody();
            tokenType = claims.getSubject();
        } catch (Exception e){
            e.printStackTrace();
            throw new NotValidTokenException(ErrorCode.NOT_VALID_TOKEN);
        }
        return tokenType;
    }

}