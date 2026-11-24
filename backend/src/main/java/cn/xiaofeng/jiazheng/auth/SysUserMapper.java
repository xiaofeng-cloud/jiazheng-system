package cn.xiaofeng.jiazheng.auth;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SysUserMapper {
    @Select("SELECT u.id, u.username, u.password, CASE u.user_type WHEN 1 THEN 'ADMIN' WHEN 2 THEN 'PROVIDER' ELSE 'CUSTOMER' END AS role_code FROM sys_user u WHERE u.username = #{username} AND u.status = 1")
    UserRow findByUsername(String username);
    record UserRow(Long id, String username, String password, String roleCode) {}
}
