package cn.xiaofeng.jiazheng.serviceitem;

import cn.xiaofeng.jiazheng.common.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/service-items")
public class ServiceItemController {
    private final ServiceItemMapper mapper;
    public ServiceItemController(ServiceItemMapper mapper) { this.mapper = mapper; }

    @GetMapping
    public ApiResponse<List<ServiceItemMapper.ServiceItemRow>> list(@RequestParam(defaultValue = "false") boolean all) {
        return ApiResponse.success(all ? mapper.findAll() : mapper.findEnabled());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Long> create(@Valid @RequestBody CreateRequest request) {
        long id = mapper.nextId();
        mapper.insert(new ServiceItemMapper.ServiceItemRow(id, request.categoryId(), request.itemName(), request.serviceDuration(), request.description(), 1));
        return ApiResponse.success("服务项目已创建", id);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> changeStatus(@PathVariable Long id, @RequestParam @NotNull @Min(0) Integer status) {
        if (mapper.updateStatus(id, status) != 1) throw new IllegalArgumentException("服务项目不存在");
        return ApiResponse.success("服务项目状态已更新", null);
    }

    public record CreateRequest(@NotNull Long categoryId, @NotBlank(message = "项目名称不能为空") String itemName,
                                @NotNull @Min(1) Integer serviceDuration, String description) {}
}
