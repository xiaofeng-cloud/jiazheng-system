package cn.xiaofeng.jiazheng.quality;

import org.springframework.stereotype.Service;

@Service
public class ComplaintService {
    private final ComplaintMapper mapper;
    public ComplaintService(ComplaintMapper mapper) { this.mapper = mapper; }
    public Long create(Long orderId, Long customerId, String type, String content) {
        ComplaintMapper.OrderInfo order = mapper.orderInfo(orderId);
        if (order == null || !customerId.equals(order.customerId())) throw new IllegalArgumentException("工单不存在或不属于当前客户");
        long id = mapper.nextId(); mapper.insert(new ComplaintMapper.ComplaintRow(id, null, orderId, customerId, type, content, "待处理", null)); return id;
    }
    public void handle(Long complaintId, Long operatorId, String status, String result) {
        if (!ListStatus.ALLOWED_SET.contains(status)) throw new IllegalArgumentException("投诉状态不正确");
        if (mapper.update(complaintId, status, result) != 1) throw new IllegalArgumentException("投诉不存在");
        mapper.insertRecord(new ComplaintMapper.RecordRow(mapper.nextRecordId(), complaintId, operatorId, "处理", result));
    }
    private enum ListStatus { ALLOWED; static final java.util.Set<String> ALLOWED_SET = java.util.Set.of("处理中", "已解决", "已关闭"); }
}
