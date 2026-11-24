package cn.xiaofeng.jiazheng.order;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WorkOrderStatusLogMapper {
    @Insert("INSERT INTO work_order_status_log(id, order_id, from_status, to_status, operator_id, remark) VALUES (#{id}, #{orderId}, #{fromStatus}, #{toStatus}, #{operatorId}, #{remark})")
    int insert(StatusLogRow row);
    record StatusLogRow(Long id, Long orderId, String fromStatus, String toStatus, Long operatorId, String remark) {}
}
