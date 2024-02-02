package com.encore.ordering.securities;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.util.Date;
@Slf4j
@Component
public class JwtProvider {
    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private int expiration;

    public String createToken(String email, String role){
        // claims: 토큰 사용자에 대한 속성이나 데이터 포함. 주로, Payload를 의미
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", role);
        Date now = new Date();
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                // 밀리초 단위: 30분을 의미
                .setExpiration(new Date(now.getTime() + expiration* 60 * 1000L))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return token;
    }
}
