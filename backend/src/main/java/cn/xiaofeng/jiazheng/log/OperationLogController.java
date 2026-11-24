package cn.xiaofeng.jiazheng.log;

import cn.xiaofeng.jiazheng.common.ApiResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/logs/operations")
@PreAuthorize("hasRole('ADMIN')")
public class OperationLogController {
    private final OperationLogMapper mapper;
    public OperationLogController(OperationLogMapper mapper) { this.mapper = mapper; }
    @GetMapping
    public ApiResponse<List<OperationLogMapper.OperationLogRow>> list() { return ApiResponse.success(mapper.findRecent()); }
}
