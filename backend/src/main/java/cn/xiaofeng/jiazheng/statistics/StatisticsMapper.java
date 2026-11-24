package cn.xiaofeng.jiazheng.statistics;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface StatisticsMapper {
    @Select("SELECT status AS label, COUNT(*) AS value FROM work_order GROUP BY status ORDER BY status")
    List<StatRow> orderStatus();
    @Select("SELECT DATE_FORMAT(created_at, '%Y-%m-%d') AS label, COUNT(*) AS value FROM work_order WHERE created_at >= DATE_SUB(CURDATE(), INTERVAL 6 DAY) GROUP BY DATE_FORMAT(created_at, '%Y-%m-%d') ORDER BY label")
    List<StatRow> orderTrend();
    @Select("SELECT COALESCE(AVG(score),0) FROM service_review")
    Double averageScore();
    @Select("SELECT COUNT(*) FROM work_order WHERE status = '已完成'")
    Long completedCount();
    @Select("SELECT COUNT(*) FROM complaint WHERE status IN ('待处理','处理中')")
    Long pendingComplaintCount();
    record StatRow(String label, Long value) {}
}
