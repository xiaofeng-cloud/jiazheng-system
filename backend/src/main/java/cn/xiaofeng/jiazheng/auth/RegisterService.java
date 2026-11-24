package cn.xiaofeng.jiazheng.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterService {
    private final SysUserMapper mapper;
    private final PasswordEncoder passwordEncoder;
    public RegisterService(SysUserMapper mapper, PasswordEncoder passwordEncoder) { this.mapper = mapper; this.passwordEncoder = passwordEncoder; }

    @Transactional
    public Long register(RegisterRequest request) {
        if (mapper.findByUsername(request.username()) != null) throw new IllegalArgumentException("用户名已存在");
        long userId = mapper.nextCustomerId();
        mapper.insertCustomer(userId, request.username(), passwordEncoder.encode(request.password()), request.realName(), request.phone());
        mapper.insertCustomerRole(userId);
        return userId;
    }
}
