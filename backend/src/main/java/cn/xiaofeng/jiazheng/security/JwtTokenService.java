package cn.xiaofeng.jiazheng.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

/** 负责创建和解析登录令牌，令牌只保存用户标识和角色。 */
public class JwtTokenService {
    private final SecretKey key;
    private final long expirationSeconds;

    public JwtTokenService(String secret, long expirationSeconds) {
        if (secret == null || secret.getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalArgumentException("JWT密钥至少需要32个字节");
        }
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationSeconds = expirationSeconds;
    }

    public String createToken(Long userId, String username, String roleCode) {
        Instant now = Instant.now();
        return Jwts.builder().subject(username).claim("userId", userId).claim("roleCode", roleCode)
                .issuedAt(Date.from(now)).expiration(Date.from(now.plusSeconds(expirationSeconds)))
                .signWith(key).compact();
    }

    public TokenClaims parseToken(String token) {
        Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
        return new TokenClaims(((Number) claims.get("userId")).longValue(), claims.getSubject(), claims.get("roleCode", String.class));
    }

    public record TokenClaims(Long userId, String username, String roleCode) {}
}
