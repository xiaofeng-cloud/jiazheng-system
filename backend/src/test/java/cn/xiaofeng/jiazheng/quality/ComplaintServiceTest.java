package cn.xiaofeng.jiazheng.quality;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class ComplaintServiceTest {
    @Test
    void rejectsUnknownComplaintStatus() {
        ComplaintMapper mapper = mock(ComplaintMapper.class);
        assertThatThrownBy(() -> new ComplaintService(mapper).handle(1L, 1L, "未知", "测试"))
                .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("状态");
    }
}
