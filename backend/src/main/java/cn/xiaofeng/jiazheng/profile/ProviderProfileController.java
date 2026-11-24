package cn.xiaofeng.jiazheng.profile;

import cn.xiaofeng.jiazheng.auth.LoginUser;
import cn.xiaofeng.jiazheng.common.ApiResponse;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/providers")
public class ProviderProfileController {
    private final ProviderProfileMapper mapper;
    public ProviderProfileController(ProviderProfileMapper mapper) { this.mapper = mapper; }
    @GetMapping
    public ApiResponse<List<ProviderProfileMapper.ProviderRow>> list() { return ApiResponse.success(mapper.findAll()); }
    @GetMapping("/me")
    @PreAuthorize("hasRole('PROVIDER')")
    public ApiResponse<ProviderProfileMapper.ProviderRow> me(org.springframework.security.core.Authentication authentication) { return ApiResponse.success(mapper.findById(((LoginUser) authentication.getPrincipal()).userId())); }
    @PutMapping("/me/status")
    @PreAuthorize("hasRole('PROVIDER')")
    public ApiResponse<Void> updateStatus(@RequestBody StatusRequest request, org.springframework.security.core.Authentication authentication) {
        if (!java.util.Set.of("空闲", "忙碌", "休假", "停用").contains(request.status())) throw new IllegalArgumentException("工作状态不正确");
        mapper.updateStatus(((LoginUser) authentication.getPrincipal()).userId(), request.status()); return ApiResponse.success("工作状态已更新", null);
    }
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> adminStatus(@PathVariable Long id, @RequestBody StatusRequest request) { mapper.updateStatus(id, request.status()); return ApiResponse.success("服务人员状态已更新", null); }
    public record StatusRequest(@NotBlank String status) {}
}
