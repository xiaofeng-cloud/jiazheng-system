package cn.xiaofeng.jiazheng.order;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WorkOrderStatusServiceTest {
    private final WorkOrderStatusService service = new WorkOrderStatusService();

    @Test
    void allowsNormalStatusTransition() {
        service.validateTransition(WorkOrderStatus.PENDING_AUDIT, WorkOrderStatus.PENDING_ASSIGN);
        service.validateTransition(WorkOrderStatus.IN_SERVICE, WorkOrderStatus.PENDING_CONFIRM);
        service.validateTransition(WorkOrderStatus.PENDING_CONFIRM, WorkOrderStatus.COMPLETED);
    }

    @Test
    void rejectsSkippingStatusOrChangingCompletedOrder() {
        assertThatThrownBy(() -> service.validateTransition(WorkOrderStatus.PENDING_AUDIT, WorkOrderStatus.IN_SERVICE))
                .isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> service.validateTransition(WorkOrderStatus.COMPLETED, WorkOrderStatus.IN_SERVICE))
                .isInstanceOf(IllegalStateException.class);
    }
}
