package cn.xiaofeng.jiazheng.scheduling;

import cn.xiaofeng.jiazheng.order.WorkOrderStatusLogMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class SchedulingService {
    private final SchedulingMapper mapper; private final WorkOrderStatusLogMapper logMapper;
    public SchedulingService(SchedulingMapper mapper, WorkOrderStatusLogMapper logMapper) { this.mapper = mapper; this.logMapper = logMapper; }
    public List<SchedulingMapper.CandidateRow> candidates(Long orderId) { return mapper.candidates(orderId); }
    @Transactional
    public void assign(Long orderId, Long providerId, Long operatorId, String reason) {
        SchedulingMapper.OrderInfo order = mapper.orderInfo(orderId);
        if (order == null) throw new IllegalArgumentException("工单不存在");
        if (!"待分配".equals(order.status()) && !"待接单".equals(order.status())) throw new IllegalStateException("当前工单不允许派单");
        if (mapper.conflict(providerId, order.scheduledDate(), order.scheduledTime(), orderId) > 0) throw new IllegalStateException("该服务人员在此时间段已有工单");
        if (mapper.assign(orderId, providerId) != 1) throw new IllegalStateException("派单失败");
        mapper.insertAssignment(new SchedulingMapper.AssignmentRow(mapper.nextAssignmentId(), orderId, providerId, "人工指定", 100D, reason == null ? "管理员人工派单" : reason));
        logMapper.insert(new WorkOrderStatusLogMapper.StatusLogRow(System.currentTimeMillis(), orderId, "待分配", "待接单", operatorId, "完成派单"));
    }
}
