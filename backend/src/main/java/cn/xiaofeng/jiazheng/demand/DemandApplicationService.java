package cn.xiaofeng.jiazheng.demand;

import cn.xiaofeng.jiazheng.order.WorkOrderMapper;
import cn.xiaofeng.jiazheng.order.WorkOrderStatusLogMapper;
import cn.xiaofeng.jiazheng.serviceitem.ServiceItemMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class DemandApplicationService {
    private final DemandMapper demandMapper;
    private final ServiceItemMapper itemMapper;
    private final WorkOrderMapper orderMapper;
    private final WorkOrderStatusLogMapper logMapper;
    public DemandApplicationService(DemandMapper demandMapper, ServiceItemMapper itemMapper, WorkOrderMapper orderMapper, WorkOrderStatusLogMapper logMapper) {
        this.demandMapper = demandMapper; this.itemMapper = itemMapper; this.orderMapper = orderMapper; this.logMapper = logMapper;
    }
    public Long create(Long customerId, Long itemId, Long addressId, LocalDate date, String time, String description) {
        if (date == null || date.isBefore(LocalDate.now())) throw new IllegalArgumentException("预约日期不能早于今天");
        if (itemMapper.findEnabledId(itemId) == null) throw new IllegalArgumentException("服务项目未启用或不存在");
        if (demandMapper.findOwnedAddress(customerId, addressId) == null) throw new IllegalArgumentException("服务地址不属于当前客户");
        long id = demandMapper.nextId();
        demandMapper.insert(new DemandMapper.DemandRow(id, "DM" + System.currentTimeMillis(), customerId, itemId, addressId, date, time, description, "待审核"));
        return id;
    }
    @Transactional
    public void approve(Long demandId, Long operatorId) {
        DemandMapper.DemandRow demand = demandMapper.findAll().stream().filter(row -> row.id().equals(demandId)).findFirst().orElseThrow(() -> new IllegalArgumentException("服务需求不存在"));
        if (!"待审核".equals(demand.status())) throw new IllegalStateException("当前需求不允许审核");
        orderMapper.insertFromDemand(demandId);
        demandMapper.updateStatus(demandId, "已生成工单");
        logMapper.insert(new WorkOrderStatusLogMapper.StatusLogRow(System.currentTimeMillis(), demandId, null, "待分配", operatorId, "需求审核通过，生成工单"));
    }
}
