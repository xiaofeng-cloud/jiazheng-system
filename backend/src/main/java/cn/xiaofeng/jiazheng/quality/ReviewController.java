package cn.xiaofeng.jiazheng.quality;

import cn.xiaofeng.jiazheng.auth.LoginUser;
import cn.xiaofeng.jiazheng.common.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService service;
    public ReviewController(ReviewService service) { this.service = service; }
    @PostMapping("/orders/{orderId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ApiResponse<Void> create(@PathVariable Long orderId, @Valid @RequestBody ReviewRequest request, org.springframework.security.core.Authentication authentication) {
        service.create(orderId, ((LoginUser) authentication.getPrincipal()).userId(), request.score(), request.content(), request.imageUrls());
        return ApiResponse.success("评价已提交", null);
    }
    @PostMapping("/orders/{orderId}/accept")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ApiResponse<Void> accept(@PathVariable Long orderId, org.springframework.security.core.Authentication authentication) {
        service.accept(orderId, ((LoginUser) authentication.getPrincipal()).userId()); return ApiResponse.success("服务已验收", null);
    }
    public record ReviewRequest(@Min(1) @Max(5) Integer score, String content, String imageUrls) {}
}
