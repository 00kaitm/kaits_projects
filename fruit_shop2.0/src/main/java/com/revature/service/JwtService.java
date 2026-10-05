package com.revature.service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.revature.exceptions.BadTokenException;
import com.revature.models.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private static final long ONE_HOUR_MS = 60 * 60 * 1000L;

    private final SecretKey key;

    public JwtService(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(User user) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(String.valueOf(user.getId()))
                .claim("role", user.getRole().name())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ONE_HOUR_MS))
                .signWith(key)
                .compact();
    }

    public int getUserId(String token) throws BadTokenException {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return Integer.parseInt(claims.getSubject());
        } catch (JwtException | IllegalArgumentException e) {
            throw new BadTokenException();
        }
    }
}