package cn.xiaofeng.jiazheng.security;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenServiceTest {

    @Test
    void shouldCreateAndParseToken() {
        JwtTokenService service = new JwtTokenService("test-secret-key-for-jiazheng-system", 3600);

        String token = service.createToken(1001L, "customer01", "CUSTOMER");
        JwtTokenService.TokenClaims claims = service.parseToken(token);

        assertThat(claims.userId()).isEqualTo(1001L);
        assertThat(claims.username()).isEqualTo("customer01");
        assertThat(claims.roleCode()).isEqualTo("CUSTOMER");
    }
}
