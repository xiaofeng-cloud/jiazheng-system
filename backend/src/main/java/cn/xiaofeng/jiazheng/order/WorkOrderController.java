package cn.xiaofeng.jiazheng.order;

import jakarta.validation.constraints.NotNull;
import cn.xiaofeng.jiazheng.common.ApiResponse;
import cn.xiaofeng.jiazheng.auth.LoginUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/work-orders")
public class WorkOrderController {
    private final WorkOrderApplicationService service; private final WorkOrderMapper mapper;
    public WorkOrderController(WorkOrderApplicationService service, WorkOrderMapper mapper) { this.service = service; this.mapper = mapper; }

    @GetMapping
    public ApiResponse<java.util.List<WorkOrderMapper.WorkOrderRow>> list(org.springframework.security.core.Authentication authentication) {
        LoginUser user = (LoginUser) authentication.getPrincipal();
        return ApiResponse.success("ADMIN".equals(user.roleCode()) ? mapper.findAll() : mapper.findForUser(user.userId()));
    }

    @PutMapping("/{orderId}/status")
    @PreAuthorize("hasAnyRole('ADMIN','PROVIDER','CUSTOMER')")
    public ApiResponse<Void> changeStatus(@PathVariable Long orderId, @RequestBody ChangeStatusRequest request,
                             org.springframework.security.core.Authentication authentication) {
        long operatorId = "admin".equals(authentication.getName()) ? 1L : 0L;
        service.changeStatus(orderId, request.status(), operatorId, request.remark());
        return ApiResponse.success("工单状态已更新", null);
    }

    @PostMapping("/{orderId}/accept")
    @PreAuthorize("hasRole('PROVIDER')")
    public ApiResponse<Void> accept(@PathVariable Long orderId, org.springframework.security.core.Authentication authentication) {
        service.accept(orderId, ((LoginUser) authentication.getPrincipal()).userId()); return ApiResponse.success("工单已接单", null);
    }
    @PostMapping("/{orderId}/reject")
    @PreAuthorize("hasRole('PROVIDER')")
    public ApiResponse<Void> reject(@PathVariable Long orderId, @RequestBody(required = false) RejectRequest request, org.springframework.security.core.Authentication authentication) {
        service.reject(orderId, ((LoginUser) authentication.getPrincipal()).userId(), request == null ? null : request.reason()); return ApiResponse.success("工单已退回调度池", null);
    }
    @PostMapping("/{orderId}/cancel")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ApiResponse<Void> cancel(@PathVariable Long orderId, @RequestBody(required = false) RejectRequest request, org.springframework.security.core.Authentication authentication) {
        service.cancelByCustomer(orderId, ((LoginUser) authentication.getPrincipal()).userId(), request == null ? "客户取消" : request.reason()); return ApiResponse.success("工单已取消", null);
    }
    public record ChangeStatusRequest(@NotNull WorkOrderStatus status, String remark) {}
    public record RejectRequest(String reason) {}
}
