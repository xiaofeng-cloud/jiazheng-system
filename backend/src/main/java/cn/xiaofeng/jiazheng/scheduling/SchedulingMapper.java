package cn.xiaofeng.jiazheng.scheduling;

import org.apache.ibatis.annotations.*;
import java.time.LocalDate;
import java.util.List;

@Mapper
public interface SchedulingMapper {
    @Select("SELECT p.user_id AS providerId, u.real_name AS providerName, p.skill_level AS skillLevel, p.work_status AS workStatus, p.service_area_id AS regionId, r.region_name AS regionName, COALESCE((SELECT COUNT(*) FROM work_order w2 WHERE w2.provider_id = p.user_id AND w2.scheduled_date = (SELECT scheduled_date FROM work_order WHERE id = #{orderId}) AND w2.scheduled_time = (SELECT scheduled_time FROM work_order WHERE id = #{orderId}) AND w2.status NOT IN ('已完成','已取消')), 0) AS loadCount FROM provider_profile p JOIN sys_user u ON u.id = p.user_id LEFT JOIN service_region r ON r.id = p.service_area_id WHERE p.work_status <> '停用' ORDER BY loadCount, p.user_id")
    List<CandidateRow> candidates(Long orderId);
    @Select("SELECT scheduled_date AS scheduledDate, scheduled_time AS scheduledTime, service_item_id AS serviceItemId, status FROM work_order WHERE id = #{orderId}")
    OrderInfo orderInfo(Long orderId);
    @Select("SELECT COUNT(*) FROM work_order WHERE provider_id = #{providerId} AND scheduled_date = #{date} AND scheduled_time = #{time} AND status NOT IN ('已完成','已取消') AND id <> #{orderId}")
    int conflict(Long providerId, LocalDate date, String time, Long orderId);
    @Update("UPDATE work_order SET provider_id = #{providerId}, status = '待接单', updated_at = NOW() WHERE id = #{orderId} AND status IN ('待分配','待接单')")
    int assign(Long orderId, Long providerId);
    @Insert("INSERT INTO work_order_assignment(id, order_id, provider_id, assign_type, match_score, match_reason, assign_status) VALUES (#{id}, #{orderId}, #{providerId}, #{assignType}, #{score}, #{reason}, '已分配')")
    int insertAssignment(AssignmentRow row);
    @Select("SELECT COALESCE(MAX(id),0)+1 FROM work_order_assignment")
    Long nextAssignmentId();
    record CandidateRow(Long providerId, String providerName, String skillLevel, String workStatus, Long regionId, String regionName, Long loadCount) {}
    record OrderInfo(LocalDate scheduledDate, String scheduledTime, Long serviceItemId, String status) {}
    record AssignmentRow(Long id, Long orderId, Long providerId, String assignType, Double score, String reason) {}
}
