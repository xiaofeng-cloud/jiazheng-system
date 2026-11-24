package cn.xiaofeng.jiazheng.statistics;

import cn.xiaofeng.jiazheng.common.ApiResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
@PreAuthorize("hasRole('ADMIN')")
public class StatisticsController {
    private final StatisticsMapper mapper;
    public StatisticsController(StatisticsMapper mapper) { this.mapper = mapper; }
    @GetMapping("/dashboard")
    public ApiResponse<Map<String, Object>> dashboard() {
        return ApiResponse.success(Map.of("orderStatus", mapper.orderStatus(), "orderTrend", mapper.orderTrend(), "averageScore", mapper.averageScore() == null ? 0D : mapper.averageScore(), "completedCount", mapper.completedCount() == null ? 0L : mapper.completedCount(), "pendingComplaintCount", mapper.pendingComplaintCount() == null ? 0L : mapper.pendingComplaintCount()));
    }
}
