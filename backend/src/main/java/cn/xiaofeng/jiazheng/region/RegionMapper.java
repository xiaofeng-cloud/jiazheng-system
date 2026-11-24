package cn.xiaofeng.jiazheng.region;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface RegionMapper {
    @Select("SELECT id, region_name AS regionName, province, city, district FROM service_region WHERE status = 1 ORDER BY id")
    List<RegionRow> findEnabled();
    record RegionRow(Long id, String regionName, String province, String city, String district) {}
}
