package cn.xiaofeng.jiazheng.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {
    private final SysUserMapper mapper;
    public DatabaseUserDetailsService(SysUserMapper mapper) { this.mapper = mapper; }
    @Override public UserDetails loadUserByUsername(String username) {
        SysUserMapper.UserRow row = mapper.findByUsername(username);
        if (row == null) throw new UsernameNotFoundException("用户不存在或已停用");
        return new LoginUser(row.id(), row.username(), row.password(), row.roleCode());
    }
}
