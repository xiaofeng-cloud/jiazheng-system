package cn.xiaofeng.jiazheng.order;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/** 集中维护工单状态机，防止接口直接跳过审核或验收环节。 */
public class WorkOrderStatusService {
    private final Map<WorkOrderStatus, Set<WorkOrderStatus>> transitions = Map.of(
            WorkOrderStatus.PENDING_AUDIT, EnumSet.of(WorkOrderStatus.PENDING_ASSIGN, WorkOrderStatus.CANCELLED),
            WorkOrderStatus.PENDING_ASSIGN, EnumSet.of(WorkOrderStatus.PENDING_ACCEPT, WorkOrderStatus.CANCELLED),
            WorkOrderStatus.PENDING_ACCEPT, EnumSet.of(WorkOrderStatus.ACCEPTED, WorkOrderStatus.PENDING_ASSIGN, WorkOrderStatus.CANCELLED),
            WorkOrderStatus.ACCEPTED, EnumSet.of(WorkOrderStatus.IN_SERVICE, WorkOrderStatus.CANCELLED),
            WorkOrderStatus.IN_SERVICE, EnumSet.of(WorkOrderStatus.PENDING_CONFIRM, WorkOrderStatus.CANCELLED),
            WorkOrderStatus.PENDING_CONFIRM, EnumSet.of(WorkOrderStatus.COMPLETED, WorkOrderStatus.IN_SERVICE),
            WorkOrderStatus.COMPLETED, EnumSet.noneOf(WorkOrderStatus.class),
            WorkOrderStatus.CANCELLED, EnumSet.noneOf(WorkOrderStatus.class));

    public void validateTransition(WorkOrderStatus from, WorkOrderStatus to) {
        if (!transitions.getOrDefault(from, Set.of()).contains(to)) {
            throw new IllegalStateException("工单状态不能从" + from.text() + "变更为" + to.text());
        }
    }
}
