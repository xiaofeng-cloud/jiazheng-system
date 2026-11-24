package cn.xiaofeng.jiazheng.demand;

import cn.xiaofeng.jiazheng.order.WorkOrderMapper;
import cn.xiaofeng.jiazheng.order.WorkOrderStatusLogMapper;
import cn.xiaofeng.jiazheng.serviceitem.ServiceItemMapper;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class DemandApplicationServiceTest {
    @Test
    void rejectsAddressOwnedByAnotherCustomer() {
        DemandMapper demand = mock(DemandMapper.class); ServiceItemMapper items = mock(ServiceItemMapper.class);
        when(items.findEnabledId(1L)).thenReturn(1L); when(demand.findOwnedAddress(1001L, 9L)).thenReturn(null);
        DemandApplicationService service = new DemandApplicationService(demand, items, mock(WorkOrderMapper.class), mock(WorkOrderStatusLogMapper.class));
        assertThatThrownBy(() -> service.create(1001L, 1L, 9L, LocalDate.now().plusDays(1), "09:00-12:00", "测试"))
                .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("不属于");
    }
}
