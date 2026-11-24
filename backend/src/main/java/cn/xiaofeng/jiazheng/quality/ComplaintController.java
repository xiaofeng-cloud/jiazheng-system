package cn.xiaofeng.jiazheng.quality;

import cn.xiaofeng.jiazheng.auth.LoginUser;
import cn.xiaofeng.jiazheng.common.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {
    private final ComplaintMapper mapper; private final ComplaintService service;
    public ComplaintController(ComplaintMapper mapper, ComplaintService service) { this.mapper = mapper; this.service = service; }
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<ComplaintMapper.ComplaintRow>> list() { return ApiResponse.success(mapper.findAll()); }
    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ApiResponse<Long> create(@Valid @RequestBody ComplaintRequest request, org.springframework.security.core.Authentication authentication) {
        return ApiResponse.success("投诉已提交", service.create(request.orderId(), ((LoginUser) authentication.getPrincipal()).userId(), request.complaintType(), request.content()));
    }
    @PutMapping("/{id}/handle")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> handle(@PathVariable Long id, @Valid @RequestBody HandleRequest request, org.springframework.security.core.Authentication authentication) {
        service.handle(id, ((LoginUser) authentication.getPrincipal()).userId(), request.status(), request.result()); return ApiResponse.success("投诉处理记录已保存", null);
    }
    public record ComplaintRequest(@NotBlank String complaintType, @NotBlank String content, @NotNull Long orderId) {}
    public record HandleRequest(@NotBlank String status, @NotBlank String result) {}
}
