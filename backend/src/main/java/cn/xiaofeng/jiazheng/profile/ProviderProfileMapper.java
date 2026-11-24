package cn.xiaofeng.jiazheng.profile;

import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface ProviderProfileMapper {
    @Select("SELECT p.user_id AS userId, u.real_name AS realName, u.phone, p.skill_level AS skillLevel, p.work_status AS workStatus, p.years_experience AS yearsExperience, p.service_area_id AS serviceAreaId, p.training_status AS trainingStatus, p.introduction FROM provider_profile p JOIN sys_user u ON u.id = p.user_id WHERE u.status = 1 ORDER BY p.user_id")
    List<ProviderRow> findAll();
    @Select("SELECT p.user_id AS userId, u.real_name AS realName, u.phone, p.skill_level AS skillLevel, p.work_status AS workStatus, p.years_experience AS yearsExperience, p.service_area_id AS serviceAreaId, p.training_status AS trainingStatus, p.introduction FROM provider_profile p JOIN sys_user u ON u.id = p.user_id WHERE p.user_id = #{userId}")
    ProviderRow findById(Long userId);
    @Update("UPDATE provider_profile SET work_status = #{status} WHERE user_id = #{userId}")
    int updateStatus(Long userId, String status);
    record ProviderRow(Long userId, String realName, String phone, String skillLevel, String workStatus, Integer yearsExperience, Long serviceAreaId, String trainingStatus, String introduction) {}
}
