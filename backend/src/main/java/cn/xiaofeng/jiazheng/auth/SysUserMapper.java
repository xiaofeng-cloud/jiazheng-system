package cn.xiaofeng.jiazheng.auth;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SysUserMapper {
    @Select("SELECT u.id, u.username, u.password, CASE u.user_type WHEN 1 THEN 'ADMIN' WHEN 2 THEN 'PROVIDER' ELSE 'CUSTOMER' END AS role_code FROM sys_user u WHERE u.username = #{username} AND u.status = 1")
    UserRow findByUsername(String username);
    @Select("SELECT COALESCE(MAX(id), 1000) + 1 FROM sys_user WHERE user_type = 3")
    Long nextCustomerId();
    @org.apache.ibatis.annotations.Insert("INSERT INTO sys_user(id, username, password, real_name, phone, user_type, status) VALUES (#{id}, #{username}, #{password}, #{realName}, #{phone}, 3, 1)")
    int insertCustomer(Long id, String username, String password, String realName, String phone);
    @org.apache.ibatis.annotations.Insert("INSERT INTO sys_user_role(user_id, role_id) VALUES (#{userId}, 3)")
    int insertCustomerRole(Long userId);
    record UserRow(Long id, String username, String password, String roleCode) {}
}
