package cn.xiaofeng.jiazheng.order;

/** 工单状态，与数据库中的中文状态值保持一致。 */
public enum WorkOrderStatus {
    PENDING_AUDIT("待审核"), PENDING_ASSIGN("待分配"), PENDING_ACCEPT("待接单"), ACCEPTED("已接单"),
    IN_SERVICE("服务中"), PENDING_CONFIRM("待确认"), COMPLETED("已完成"), CANCELLED("已取消");

    private final String text;
    WorkOrderStatus(String text) { this.text = text; }
    public String text() { return text; }
}
