package cn.xiaofeng.jiazheng.scheduling;

import cn.xiaofeng.jiazheng.auth.LoginUser;
import cn.xiaofeng.jiazheng.common.ApiResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/scheduling")
public class SchedulingController {
    private final SchedulingService service;
    public SchedulingController(SchedulingService service) { this.service = service; }
    @GetMapping("/orders/{orderId}/candidates")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<SchedulingMapper.CandidateRow>> candidates(@PathVariable Long orderId) { return ApiResponse.success(service.candidates(orderId)); }
    @PostMapping("/orders/{orderId}/assign")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> assign(@PathVariable Long orderId, @RequestBody AssignRequest request, org.springframework.security.core.Authentication authentication) {
        service.assign(orderId, request.providerId(), ((LoginUser) authentication.getPrincipal()).userId(), request.reason());
        return ApiResponse.success("工单已派发", null);
    }
    public record AssignRequest(@NotNull Long providerId, String reason) {}
}
