package cn.xiaofeng.jiazheng.quality;

import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface ReviewMapper {
    @Select("SELECT status, customer_id AS customerId, provider_id AS providerId FROM work_order WHERE id = #{orderId}")
    OrderInfo orderInfo(Long orderId);
    @Select("SELECT COUNT(*) FROM service_review WHERE order_id = #{orderId}")
    int exists(Long orderId);
    @Update("UPDATE work_order SET status = '已完成', completed_at = NOW(), updated_at = NOW() WHERE id = #{orderId} AND customer_id = #{customerId} AND status = '待确认'")
    int accept(Long orderId, Long customerId);
    @Insert("INSERT INTO service_review(id, order_id, customer_id, provider_id, score, content, image_urls) VALUES (#{id}, #{orderId}, #{customerId}, #{providerId}, #{score}, #{content}, #{imageUrls})")
    int insert(ReviewRow row);
    @Select("SELECT COALESCE(MAX(id),0)+1 FROM service_review")
    Long nextId();
    @Select("SELECT COALESCE(AVG(score),0) FROM service_review WHERE provider_id = #{providerId}")
    Double averageScore(Long providerId);
    record OrderInfo(String status, Long customerId, Long providerId) {}
    record ReviewRow(Long id, Long orderId, Long customerId, Long providerId, Integer score, String content, String imageUrls) {}
}
