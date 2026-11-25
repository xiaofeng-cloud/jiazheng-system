-- 导入后执行此文件，检查初始化数据规模和关键外键关联。
USE jiazheng_system;
SELECT '客户数' AS 指标, COUNT(*) AS 数量 FROM customer_profile
UNION ALL SELECT '服务人员数', COUNT(*) FROM provider_profile
UNION ALL SELECT '服务项目数', COUNT(*) FROM service_item
UNION ALL SELECT '服务区域数', COUNT(*) FROM service_region
UNION ALL SELECT '需求数', COUNT(*) FROM service_demand
UNION ALL SELECT '工单数', COUNT(*) FROM work_order
UNION ALL SELECT '调度记录数', COUNT(*) FROM work_order_assignment
UNION ALL SELECT '过程记录数', COUNT(*) FROM service_process
UNION ALL SELECT '评价数', COUNT(*) FROM service_review
UNION ALL SELECT '投诉数', COUNT(*) FROM complaint
UNION ALL SELECT '公告数', COUNT(*) FROM sys_notice;

SELECT '孤立工单' AS 检查项, COUNT(*) AS 异常数 FROM work_order w LEFT JOIN service_demand d ON d.id = w.demand_id WHERE d.id IS NULL
UNION ALL SELECT '孤立评价', COUNT(*) FROM service_review r LEFT JOIN work_order w ON w.id = r.order_id WHERE w.id IS NULL
UNION ALL SELECT '孤立投诉', COUNT(*) FROM complaint c LEFT JOIN work_order w ON w.id = c.order_id WHERE w.id IS NULL
UNION ALL SELECT '孤立过程记录', COUNT(*) FROM service_process p LEFT JOIN work_order w ON w.id = p.order_id WHERE w.id IS NULL;

SELECT status AS 工单状态, COUNT(*) AS 数量 FROM work_order GROUP BY status ORDER BY status;
