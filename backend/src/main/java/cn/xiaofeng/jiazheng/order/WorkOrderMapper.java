package cn.xiaofeng.jiazheng.order;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface WorkOrderMapper {
    @Select("SELECT id, status FROM work_order WHERE id = #{id}")
    WorkOrderRow findStatus(Long id);
    @Update("UPDATE work_order SET status = #{status}, updated_at = NOW() WHERE id = #{id}")
    int updateStatus(Long id, String status);
    record WorkOrderRow(Long id, String status) {}
}
