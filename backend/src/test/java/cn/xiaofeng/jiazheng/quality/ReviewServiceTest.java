package cn.xiaofeng.jiazheng.quality;

import org.junit.jupiter.api.Test;
import cn.xiaofeng.jiazheng.order.WorkOrderStatusLogMapper;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class ReviewServiceTest {
    @Test
    void preventsDuplicateReview() {
        ReviewMapper mapper = mock(ReviewMapper.class);
        when(mapper.orderInfo(1L)).thenReturn(new ReviewMapper.OrderInfo("已完成", 1001L, 2001L));
        when(mapper.exists(1L)).thenReturn(1);
        assertThatThrownBy(() -> new ReviewService(mapper, mock(WorkOrderStatusLogMapper.class)).create(1L, 1001L, 5, "满意", null))
                .isInstanceOf(IllegalStateException.class).hasMessageContaining("已评价");
    }
}
