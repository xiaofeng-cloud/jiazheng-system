package cn.xiaofeng.jiazheng.auth;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DatabaseUserDetailsServiceTest {
    @Test
    void mapsDatabaseRoleToSpringAuthority() {
        SysUserMapper mapper = mock(SysUserMapper.class);
        when(mapper.findByUsername("customer01")).thenReturn(new SysUserMapper.UserRow(1001L, "customer01", "hash", "CUSTOMER"));

        LoginUser user = (LoginUser) new DatabaseUserDetailsService(mapper).loadUserByUsername("customer01");

        assertThat(user.userId()).isEqualTo(1001L);
        assertThat(user.getAuthorities()).extracting("authority").containsExactly("ROLE_CUSTOMER");
    }
}
