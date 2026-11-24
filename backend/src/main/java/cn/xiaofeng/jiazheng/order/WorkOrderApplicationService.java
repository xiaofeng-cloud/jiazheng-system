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
        WorkOrderMapper.WorkOrderRow row = orderMapper.findStatus(orderId);
        if (row == null) throw new IllegalArgumentException("工单不存在");
        WorkOrderStatus from = parse(row.status());
        statusService.validateTransition(from, target);
        if (orderMapper.updateStatus(orderId, target.text()) != 1) throw new IllegalStateException("工单更新失败");
        logMapper.insert(new WorkOrderStatusLogMapper.StatusLogRow(logId.incrementAndGet(), orderId, from.text(), target.text(), operatorId, remark));
    }

    private WorkOrderStatus parse(String text) {
        for (WorkOrderStatus value : WorkOrderStatus.values()) if (value.text().equals(text)) return value;
        throw new IllegalArgumentException("未知工单状态：" + text);
    }
}
