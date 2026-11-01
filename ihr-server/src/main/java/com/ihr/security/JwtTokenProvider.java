package com.ihr.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

/**
 * JWT 生成与解析工具。
 * <p>Claims 约定：sub = 用户名；roles = 角色编码列表。</p>
 */
@Component
public class JwtTokenProvider {

    private final SecretKey key;
    private final long expireMillis;

    public JwtTokenProvider(JwtProperties properties) {
        this.key = Keys.hmacShaKeyFor(properties.getSecret().getBytes(StandardCharsets.UTF_8));
        this.expireMillis = properties.getExpire();
    }

    /** 生成 token */
    public String createToken(String username, List<String> roles) {
        Date now = new Date();
        Date expireAt = new Date(now.getTime() + expireMillis);
        return Jwts.builder()
                .subject(username)
                .claim("roles", roles)
                .issuedAt(now)
                .expiration(expireAt)
                .signWith(key)
                .compact();
    }

    /** 解析 token，失败（过期/伪造/格式错误）抛出 JwtException */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /** 从 claims 中取角色列表 */
    @SuppressWarnings("unchecked")
    public List<String> extractRoles(Claims claims) {
        Object roles = claims.get("roles");
        return roles instanceof List ? (List<String>) roles : List.of();
    }

    /** token 有效期（毫秒），供前端倒计时使用 */
    public long getExpireMillis() {
        return expireMillis;
    }

    /** 供过滤器判断异常类型使用 */
    public boolean isJwtException(Exception e) {
        return e instanceof JwtException || e instanceof IllegalArgumentException;
    }
}
