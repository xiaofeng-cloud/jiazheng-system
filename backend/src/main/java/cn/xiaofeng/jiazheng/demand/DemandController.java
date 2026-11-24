package cn.xiaofeng.jiazheng.demand;

import cn.xiaofeng.jiazheng.auth.LoginUser;
import cn.xiaofeng.jiazheng.common.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/demands")
public class DemandController {
    private final DemandMapper mapper; private final DemandApplicationService service;
    public DemandController(DemandMapper mapper, DemandApplicationService service) { this.mapper = mapper; this.service = service; }
    @GetMapping
    public ApiResponse<List<DemandMapper.DemandRow>> list(org.springframework.security.core.Authentication authentication) {
        LoginUser user = (LoginUser) authentication.getPrincipal();
        return ApiResponse.success("ADMIN".equals(user.roleCode()) ? mapper.findAll() : mapper.findByCustomer(user.userId()));
    }
    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ApiResponse<Long> create(@Valid @RequestBody CreateRequest request, org.springframework.security.core.Authentication authentication) {
        long customerId = ((LoginUser) authentication.getPrincipal()).userId();
        return ApiResponse.success("服务需求已提交", service.create(customerId, request.serviceItemId(), request.addressId(), request.expectedDate(), request.expectedTime(), request.description()));
    }
    @PostMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> approve(@PathVariable Long id, org.springframework.security.core.Authentication authentication) {
        service.approve(id, ((LoginUser) authentication.getPrincipal()).userId()); return ApiResponse.success("需求审核通过", null);
    }
    public record CreateRequest(@NotNull Long serviceItemId, @NotNull Long addressId, @NotNull @FutureOrPresent LocalDate expectedDate, String expectedTime, String description) {}
}
