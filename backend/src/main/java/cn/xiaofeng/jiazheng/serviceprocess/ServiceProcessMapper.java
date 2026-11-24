package cn.xiaofeng.jiazheng.serviceprocess;

import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface ServiceProcessMapper {
    @Select("SELECT COUNT(*) FROM work_order WHERE id = #{orderId} AND provider_id = #{providerId}")
    int assigned(Long orderId, Long providerId);
    @Insert("INSERT INTO service_process(id, order_id, provider_id, process_type, process_time, description, longitude, latitude) VALUES (#{id}, #{orderId}, #{providerId}, #{processType}, NOW(), #{description}, #{longitude}, #{latitude})")
    int insert(ProcessRow row);
    @Select("SELECT id, order_id AS orderId, provider_id AS providerId, process_type AS processType, process_time AS processTime, description, longitude, latitude FROM service_process WHERE order_id = #{orderId} ORDER BY process_time")
    List<ProcessRow> findByOrder(Long orderId);
    @Select("SELECT COALESCE(MAX(id),0)+1 FROM service_process")
    Long nextId();
    @Insert("INSERT INTO service_image(id, order_id, process_id, image_type, image_url, description) VALUES (#{id}, #{orderId}, #{processId}, #{imageType}, #{imageUrl}, #{description})")
    int insertImage(ImageRow row);
    @Select("SELECT COALESCE(MAX(id),0)+1 FROM service_image")
    Long nextImageId();
    record ProcessRow(Long id, Long orderId, Long providerId, String processType, java.time.LocalDateTime processTime, String description, Double longitude, Double latitude) {}
    record ImageRow(Long id, Long orderId, Long processId, String imageType, String imageUrl, String description) {}
}
