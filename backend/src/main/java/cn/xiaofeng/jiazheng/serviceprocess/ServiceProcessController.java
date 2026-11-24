package cn.xiaofeng.jiazheng.serviceprocess;

import cn.xiaofeng.jiazheng.auth.LoginUser;
import cn.xiaofeng.jiazheng.common.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/service-process")
public class ServiceProcessController {
    private final ServiceProcessService service;
    public ServiceProcessController(ServiceProcessService service) { this.service = service; }
    @GetMapping("/orders/{orderId}")
    public ApiResponse<List<ServiceProcessMapper.ProcessRow>> timeline(@PathVariable Long orderId) { return ApiResponse.success(service.timeline(orderId)); }
    @PostMapping("/orders/{orderId}")
    @PreAuthorize("hasRole('PROVIDER')")
    public ApiResponse<Long> record(@PathVariable Long orderId, @Valid @RequestBody ProcessRequest request, org.springframework.security.core.Authentication authentication) {
        Long providerId = ((LoginUser) authentication.getPrincipal()).userId();
        return ApiResponse.success("服务过程已记录", service.record(orderId, providerId, request.processType(), request.description(), request.longitude(), request.latitude()));
    }
    @PostMapping("/orders/{orderId}/images")
    @PreAuthorize("hasAnyRole('PROVIDER','ADMIN')")
    public ApiResponse<Void> image(@PathVariable Long orderId, @RequestBody ImageRequest request) { service.addImage(orderId, request.processId(), request.imageType(), request.imageUrl(), request.description()); return ApiResponse.success("图片记录已保存", null); }
    public record ProcessRequest(@NotBlank(message = "过程类型不能为空") String processType, String description, Double longitude, Double latitude) {}
    public record ImageRequest(Long processId, @NotBlank String imageType, @NotBlank String imageUrl, String description) {}
}
