# 家政服务工单调度与服务质量追踪管理系统

本项目面向中国大陆家政服务企业，提供客户需求、服务工单、人员调度、服务过程、验收评价、投诉处理和质量统计能力。

当前项目按前后端分离方式建设：

- `backend`：Java 17、Spring Boot 3、Maven 后端服务
- `frontend`：Vue 3、Vite、Element Plus 前端应用
- `database`：MySQL 8.0 建表脚本和初始化数据
- `docs`：需求、设计、接口、部署和测试文档

## 业务边界

系统包含客户、家政服务人员和系统管理员三类角色，围绕“需求提交 → 审核 → 派单 → 接单 → 服务 → 验收 → 评价 → 投诉处理 → 统计分析”形成闭环。

系统只处理服务工单和质量追踪，不包含微信小程序、在线支付、充值、退款、押金、钱包、提现或服务商结算。

## 当前开发状态

已完成项目骨架、MySQL 结构与演示数据、JWT 角色认证、工单状态机和中文前端工作台。项目继续按 `docs/superpowers/plans/2026-11-24-jiazheng-system.md` 分阶段实施，每完成一个阶段都会补充测试和文档。

## 本地环境

- JDK 17+
- Maven 3.9+
- Node.js 20+
- MySQL 8.0+

## 启动方式

先按 `database/README.md` 导入数据库，再复制 `backend/src/main/resources/application-local.yml.example` 为 `application-local.yml`，填写本机 MySQL 密码。

```bash
cd backend
mvn spring-boot:run

# 新开终端
cd frontend
npm install
npm run dev
```

浏览器访问 Vite 输出的地址，演示账号为 `admin`、`customer01`、`provider01`，密码均为 `123456`。后端接口清单见 `docs/后端接口说明.md`。

## 中文代码说明

关键类、方法、数据库表和业务状态都会添加中文说明，便于本科毕业设计学习、演示和后续修改。
