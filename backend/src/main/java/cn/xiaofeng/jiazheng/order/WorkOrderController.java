package cn.xiaofeng.jiazheng.order;

import jakarta.validation.constraints.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/work-orders")
public class WorkOrderController {
    private final WorkOrderApplicationService service;
    public WorkOrderController(WorkOrderApplicationService service) { this.service = service; }

    @PutMapping("/{orderId}/status")
    @PreAuthorize("hasAnyRole('ADMIN','PROVIDER','CUSTOMER')")
    public void changeStatus(@PathVariable Long orderId, @RequestBody ChangeStatusRequest request,
                             org.springframework.security.core.Authentication authentication) {
        long operatorId = "admin".equals(authentication.getName()) ? 1L : 0L;
        service.changeStatus(orderId, request.status(), operatorId, request.remark());
    }
    public record ChangeStatusRequest(@NotNull WorkOrderStatus status, String remark) {}
}
