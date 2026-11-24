package cn.xiaofeng.jiazheng.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;

/** 从数据库加载的登录用户，保留用户主键供工单审计使用。 */
public class LoginUser implements UserDetails {
    private final Long userId;
    private final String username;
    private final String password;
    private final String roleCode;

    public LoginUser(Long userId, String username, String password, String roleCode) {
        this.userId = userId; this.username = username; this.password = password; this.roleCode = roleCode;
    }
    public Long userId() { return userId; }
    public String roleCode() { return roleCode; }
    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return java.util.List.of(() -> "ROLE_" + roleCode); }
    @Override public String getPassword() { return password; }
    @Override public String getUsername() { return username; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
