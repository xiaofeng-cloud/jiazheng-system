package cn.xiaofeng.jiazheng.order;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class WorkOrderApplicationService {
    private final WorkOrderMapper orderMapper;
    private final WorkOrderStatusLogMapper logMapper;
    private final WorkOrderStatusService statusService = new WorkOrderStatusService();
    private final AtomicLong logId = new AtomicLong(System.currentTimeMillis());

    public WorkOrderApplicationService(WorkOrderMapper orderMapper, WorkOrderStatusLogMapper logMapper) { this.orderMapper = orderMapper; this.logMapper = logMapper; }

    @Transactional
    public void changeStatus(Long orderId, WorkOrderStatus target, Long operatorId, String remark) {
        WorkOrderMapper.StatusRow row = orderMapper.findStatus(orderId);
        if (row == null) throw new IllegalArgumentException("工单不存在");
        WorkOrderStatus from = parse(row.status());
        statusService.validateTransition(from, target);
        if (orderMapper.updateStatus(orderId, target.text()) != 1) throw new IllegalStateException("工单更新失败");
        logMapper.insert(new WorkOrderStatusLogMapper.StatusLogRow(logId.incrementAndGet(), orderId, from.text(), target.text(), operatorId, remark));
    }

    @Transactional
    public void accept(Long orderId, Long providerId) {
        if (orderMapper.accept(orderId, providerId) != 1) throw new IllegalStateException("工单不存在、未分配给当前人员或已被处理");
        logMapper.insert(new WorkOrderStatusLogMapper.StatusLogRow(logId.incrementAndGet(), orderId, "待接单", "已接单", providerId, "服务人员接受工单"));
    }
    @Transactional
    public void reject(Long orderId, Long providerId, String reason) {
        if (orderMapper.reject(orderId, providerId) != 1) throw new IllegalStateException("工单不存在、未分配给当前人员或已被处理");
        logMapper.insert(new WorkOrderStatusLogMapper.StatusLogRow(logId.incrementAndGet(), orderId, "待接单", "待分配", providerId, reason == null ? "服务人员拒绝工单" : reason));
    }
    @Transactional
    public void cancelByCustomer(Long orderId, Long customerId, String reason) {
        if (orderMapper.cancelByCustomer(orderId, customerId, reason) != 1) throw new IllegalStateException("当前工单不允许取消或不属于当前客户");
        logMapper.insert(new WorkOrderStatusLogMapper.StatusLogRow(logId.incrementAndGet(), orderId, "当前状态", "已取消", customerId, reason));
    }

    private WorkOrderStatus parse(String text) {
        for (WorkOrderStatus value : WorkOrderStatus.values()) if (value.text().equals(text)) return value;
        throw new IllegalArgumentException("未知工单状态：" + text);
    }
}
