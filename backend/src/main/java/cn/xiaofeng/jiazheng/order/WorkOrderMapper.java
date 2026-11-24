package cn.xiaofeng.jiazheng.order;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Insert;
import java.util.List;

@Mapper
public interface WorkOrderMapper {
    @Select("SELECT id, status FROM work_order WHERE id = #{id}")
    StatusRow findStatus(Long id);
    @Update("UPDATE work_order SET status = #{status}, updated_at = NOW() WHERE id = #{id}")
    int updateStatus(Long id, String status);
    @Insert("INSERT INTO work_order(id, order_no, demand_id, customer_id, provider_id, service_item_id, address_id, status, priority, scheduled_date, scheduled_time) SELECT id, CONCAT('WO', LPAD(id, 8, '0')), id, customer_id, NULL, service_item_id, address_id, '待分配', 2, expected_date, expected_time FROM service_demand WHERE id = #{demandId}")
    int insertFromDemand(Long demandId);
    @Select("SELECT id, order_no AS orderNo, customer_id AS customerId, provider_id AS providerId, service_item_id AS serviceItemId, status, scheduled_date AS scheduledDate, scheduled_time AS scheduledTime FROM work_order ORDER BY id DESC")
    List<WorkOrderRow> findAll();
    @Select("SELECT id, order_no AS orderNo, customer_id AS customerId, provider_id AS providerId, service_item_id AS serviceItemId, status, scheduled_date AS scheduledDate, scheduled_time AS scheduledTime FROM work_order WHERE customer_id = #{userId} OR provider_id = #{userId} ORDER BY id DESC")
    List<WorkOrderRow> findForUser(Long userId);
    record StatusRow(Long id, String status) {}
    record WorkOrderRow(Long id, String orderNo, Long customerId, Long providerId, Long serviceItemId, String status, java.time.LocalDate scheduledDate, String scheduledTime) {}
}
