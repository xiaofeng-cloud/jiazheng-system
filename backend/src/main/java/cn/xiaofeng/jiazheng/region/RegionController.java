package cn.xiaofeng.jiazheng.region;

import cn.xiaofeng.jiazheng.common.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/regions")
public class RegionController {
    private final RegionMapper mapper;
    public RegionController(RegionMapper mapper) { this.mapper = mapper; }
    @GetMapping
    public ApiResponse<List<RegionMapper.RegionRow>> list() { return ApiResponse.success(mapper.findEnabled()); }
}
