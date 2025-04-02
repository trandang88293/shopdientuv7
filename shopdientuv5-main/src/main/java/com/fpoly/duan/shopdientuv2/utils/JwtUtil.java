package com.fpoly.duan.shopdientuv2.utils;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
        // Khóa bí mật: cần đảm bảo đủ độ dài cho HS256
        private static final String SECRET_KEY = "IUhuQQpG1l3gA5aFf9SjfjRau2WiXYDIORDGWkggqNBIv4aGb5";
        // Thời gian hết hạn: 7 ngày
        private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7;

        private Key getSigningKey() {
                return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
        }

        public String generateToken(String username) {
                return Jwts.builder()
                                .setSubject(username)
                                .setIssuedAt(new Date())
                                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                                .compact();
        }

        public String extractUsername(String token) {
                try {
                        return Jwts.parserBuilder()
                                        .setSigningKey(getSigningKey())
                                        .build()
                                        .parseClaimsJws(token)
                                        .getBody()
                                        .getSubject();
                } catch (Exception e) {
                        return null;
                }
        }

        public boolean validateToken(String token, String username) {
                String extractedUsername = extractUsername(token);
                return extractedUsername != null
                                && extractedUsername.equals(username)
                                && !isTokenExpired(token);
        }

        private boolean isTokenExpired(String token) {
                try {
                        Date expiration = Jwts.parserBuilder()
                                        .setSigningKey(getSigningKey())
                                        .build()
                                        .parseClaimsJws(token)
                                        .getBody()
                                        .getExpiration();
                        return expiration.before(new Date());
                } catch (Exception e) {
                        return true;
                }
        }
}
