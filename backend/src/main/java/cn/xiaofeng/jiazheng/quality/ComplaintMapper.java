package cn.xiaofeng.jiazheng.quality;

import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface ComplaintMapper {
    @Select("SELECT status, customer_id AS customerId FROM work_order WHERE id = #{orderId}")
    OrderInfo orderInfo(Long orderId);
    @Insert("INSERT INTO complaint(id, complaint_no, order_id, customer_id, complaint_type, content, status) VALUES (#{id}, CONCAT('CP', LPAD(#{id}, 8, '0')), #{orderId}, #{customerId}, #{type}, #{content}, '待处理')")
    int insert(ComplaintRow row);
    @Select("SELECT COALESCE(MAX(id),0)+1 FROM complaint")
    Long nextId();
    @Select("SELECT id, complaint_no AS complaintNo, order_id AS orderId, customer_id AS customerId, complaint_type AS complaintType, content, status, result FROM complaint ORDER BY id DESC")
    List<ComplaintRow> findAll();
    @Update("UPDATE complaint SET status = #{status}, result = #{result} WHERE id = #{id}")
    int update(Long id, String status, String result);
    @Insert("INSERT INTO complaint_record(id, complaint_id, operator_id, action, content) VALUES (#{id}, #{complaintId}, #{operatorId}, #{action}, #{content})")
    int insertRecord(RecordRow row);
    @Select("SELECT COALESCE(MAX(id),0)+1 FROM complaint_record")
    Long nextRecordId();
    record OrderInfo(String status, Long customerId) {}
    record ComplaintRow(Long id, String complaintNo, Long orderId, Long customerId, String complaintType, String content, String status, String result) {}
    record RecordRow(Long id, Long complaintId, Long operatorId, String action, String content) {}
}
