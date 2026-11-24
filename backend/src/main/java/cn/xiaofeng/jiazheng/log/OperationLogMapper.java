package cn.xiaofeng.jiazheng.log;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface OperationLogMapper {
    @org.apache.ibatis.annotations.Select("SELECT COALESCE(MAX(id),0)+1 FROM operation_log")
    Long nextId();
    @org.apache.ibatis.annotations.Insert("INSERT INTO operation_log(id, operator_id, module_name, operation_name, request_method, request_url, result_status) VALUES (#{id}, #{operatorId}, #{moduleName}, #{operationName}, #{requestMethod}, #{requestUrl}, #{resultStatus})")
    int insert(OperationLogRow row);
    @Select("SELECT id, operator_id AS operatorId, module_name AS moduleName, operation_name AS operationName, request_method AS requestMethod, request_url AS requestUrl, result_status AS resultStatus, created_at AS createdAt FROM operation_log ORDER BY created_at DESC LIMIT 200")
    List<OperationLogRow> findRecent();
    record OperationLogRow(Long id, Long operatorId, String moduleName, String operationName, String requestMethod, String requestUrl, Integer resultStatus, java.time.LocalDateTime createdAt) {}
}
