package cn.xiaofeng.jiazheng.serviceprocess;

import cn.xiaofeng.jiazheng.order.WorkOrderApplicationService;
import cn.xiaofeng.jiazheng.order.WorkOrderStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ServiceProcessService {
    private final ServiceProcessMapper mapper; private final WorkOrderApplicationService orderService;
    public ServiceProcessService(ServiceProcessMapper mapper, WorkOrderApplicationService orderService) { this.mapper = mapper; this.orderService = orderService; }
    @Transactional
    public Long record(Long orderId, Long providerId, String type, String description, Double longitude, Double latitude) {
        if (mapper.assigned(orderId, providerId) != 1) throw new IllegalStateException("只能操作分配给自己的工单");
        Long processId = mapper.nextId();
        mapper.insert(new ServiceProcessMapper.ProcessRow(processId, orderId, providerId, type, null, description, longitude, latitude));
        if ("开始".equals(type)) orderService.changeStatus(orderId, WorkOrderStatus.IN_SERVICE, providerId, "服务人员开始服务");
        if ("完成".equals(type)) orderService.changeStatus(orderId, WorkOrderStatus.PENDING_CONFIRM, providerId, "服务人员提交完成");
        return processId;
    }
    public List<ServiceProcessMapper.ProcessRow> timeline(Long orderId) { return mapper.findByOrder(orderId); }
    public void addImage(Long orderId, Long processId, String type, String url, String description) {
        if (url == null || !url.startsWith("/uploads/")) throw new IllegalArgumentException("图片只允许使用上传文件标识");
        mapper.insertImage(new ServiceProcessMapper.ImageRow(mapper.nextImageId(), orderId, processId, type, url, description));
    }
}
