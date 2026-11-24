package cn.xiaofeng.jiazheng.notice;

import cn.xiaofeng.jiazheng.auth.LoginUser;
import cn.xiaofeng.jiazheng.common.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notices")
public class NoticeController {
    private final NoticeMapper mapper;
    public NoticeController(NoticeMapper mapper) { this.mapper = mapper; }
    @GetMapping
    public ApiResponse<List<NoticeMapper.NoticeRow>> list() { return ApiResponse.success(mapper.findPublished()); }
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Long> create(@Valid @RequestBody NoticeRequest request, org.springframework.security.core.Authentication authentication) {
        long id = mapper.nextId(); mapper.insert(new NoticeMapper.NoticeRow(id, request.title(), request.content(), request.noticeType(), null, ((LoginUser) authentication.getPrincipal()).userId()));
        return ApiResponse.success("公告已发布", id);
    }
    public record NoticeRequest(@NotBlank String title, @NotBlank String content, @NotBlank String noticeType) {}
}
