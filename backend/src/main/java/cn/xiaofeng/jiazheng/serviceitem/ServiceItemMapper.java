package cn.xiaofeng.jiazheng.serviceitem;

import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface ServiceItemMapper {
    @Select("SELECT id, category_id AS categoryId, item_name AS itemName, service_duration AS serviceDuration, description, status FROM service_item WHERE status = 1 ORDER BY id")
    List<ServiceItemRow> findEnabled();
    @Select("SELECT id, category_id AS categoryId, item_name AS itemName, service_duration AS serviceDuration, description, status FROM service_item ORDER BY id")
    List<ServiceItemRow> findAll();
    @Select("SELECT id FROM service_item WHERE id = #{id} AND status = 1")
    Long findEnabledId(Long id);
    @Insert("INSERT INTO service_item(id, category_id, item_name, service_duration, description, status) VALUES (#{id}, #{categoryId}, #{itemName}, #{serviceDuration}, #{description}, 1)")
    int insert(ServiceItemRow row);
    @Update("UPDATE service_item SET status = #{status} WHERE id = #{id}")
    int updateStatus(Long id, Integer status);
    @Select("SELECT COALESCE(MAX(id),0)+1 FROM service_item")
    Long nextId();
    record ServiceItemRow(Long id, Long categoryId, String itemName, Integer serviceDuration, String description, Integer status) {}
}
