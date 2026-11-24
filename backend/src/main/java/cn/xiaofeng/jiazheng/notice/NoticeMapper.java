package cn.xiaofeng.jiazheng.notice;

import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface NoticeMapper {
    @Select("SELECT id, title, content, notice_type AS noticeType, published_at AS publishedAt FROM sys_notice WHERE publish_status = 1 ORDER BY published_at DESC")
    List<NoticeRow> findPublished();
    @Insert("INSERT INTO sys_notice(id, title, content, notice_type, publish_status, publisher_id) VALUES (#{id}, #{title}, #{content}, #{noticeType}, 1, #{publisherId})")
    int insert(NoticeRow row);
    @Select("SELECT COALESCE(MAX(id),0)+1 FROM sys_notice")
    Long nextId();
    record NoticeRow(Long id, String title, String content, String noticeType, java.time.LocalDateTime publishedAt, Long publisherId) {}
}
