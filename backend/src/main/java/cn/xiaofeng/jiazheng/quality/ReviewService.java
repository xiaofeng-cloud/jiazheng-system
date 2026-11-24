package cn.xiaofeng.jiazheng.quality;

import cn.xiaofeng.jiazheng.order.WorkOrderStatusLogMapper;
import org.springframework.stereotype.Service;
import java.util.Objects;

@Service
public class ReviewService {
    private final ReviewMapper mapper; private final WorkOrderStatusLogMapper logMapper;
    public ReviewService(ReviewMapper mapper, WorkOrderStatusLogMapper logMapper) { this.mapper = mapper; this.logMapper = logMapper; }
    public void create(Long orderId, Long customerId, Integer score, String content, String imageUrls) {
        if (score == null || score < 1 || score > 5) throw new IllegalArgumentException("评分必须为1到5分");
        ReviewMapper.OrderInfo order = mapper.orderInfo(orderId);
        if (order == null || !Objects.equals(order.customerId(), customerId)) throw new IllegalArgumentException("工单不存在或不属于当前客户");
        if (!"已完成".equals(order.status())) throw new IllegalStateException("工单完成后才能评价");
        if (mapper.exists(orderId) > 0) throw new IllegalStateException("该工单已评价");
        mapper.insert(new ReviewMapper.ReviewRow(mapper.nextId(), orderId, customerId, order.providerId(), score, content, imageUrls));
    }
    public void accept(Long orderId, Long customerId) {
        if (mapper.accept(orderId, customerId) != 1) throw new IllegalStateException("当前工单不在待确认状态或不属于当前客户");
        logMapper.insert(new WorkOrderStatusLogMapper.StatusLogRow(System.currentTimeMillis(), orderId, "待确认", "已完成", customerId, "客户验收完成"));
    }
}
