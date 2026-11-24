# 家政服务工单调度系统实施计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 从空仓库开始交付一套面向中国大陆家政服务场景、可本地运行、前后端分离、具备真实业务闭环的本科毕业设计系统。

**Architecture:** 采用 `backend` 与 `frontend` 两个独立应用，后端使用 Spring Boot 3.x 分层架构和 RESTful API，前端使用 Vue 3 + Vite + Element Plus，MySQL 8.0 通过 MyBatis-Plus 持久化。权限以 Spring Security + JWT 为边界，工单状态、调度记录、服务过程和质量数据由后端统一维护。

**Tech Stack:** Java 17、Spring Boot 3.x、Maven、MySQL 8.0、MyBatis-Plus、Spring Security、JWT、Hibernate Validator、Lombok、Knife4j；Vue 3、Vite、JavaScript、Element Plus、Vue Router、Pinia、Axios、ECharts。

**Spec:** `C:\Users\30125\.codex\attachments\ea081289-06fe-49ac-adf5-58485bc0b2cf\已粘贴的文本.txt`

## Global Constraints

- 所有界面、文档、数据库注释和关键代码注释使用简体中文。
- 系统只做纯 Web 前后端分离，不开发微信小程序、APP 或微服务。
- 三类角色为客户、家政服务人员、系统管理员（兼调度职责）。
- 禁止在线支付、微信/支付宝支付、充值、退款、押金、交易、资金结算、钱包和提现。
- 可以显示参考价格，但不得产生资金流转。
- 所有测试数据为虚构的中国大陆姓名、地址、电话和身份证号，不使用真实隐私。
- 图表和列表必须来自后端真实查询，禁止使用前端硬编码业务数据。
- 关键状态变更、调度、投诉处理和管理员操作必须留痕。
- 每个阶段完成后先运行对应测试，再提交清晰的 Git 记录。

## Review Focus

- **越权访问：** 客户不能读取其他客户数据，服务人员不能读取未分配工单；由后端接口测试覆盖。
- **非法状态跳转：** 工单状态只能按允许的转换推进；由状态机单元测试覆盖。
- **并发调度：** 同一人员在同一时间段不能被重复分配；由服务层事务测试覆盖。
- **脏数据关联：** 工单、评价、投诉和过程记录不能引用不存在的父记录；由数据库约束和集成测试覆盖。
- **空数据看板：** 没有统计数据时接口返回可解释的零值和空数组，前端不应报错；由统计接口和页面测试覆盖。

---

### Task 1: 仓库骨架与开发基线

**Files:**
- Create: `README.md`
- Create: `.gitignore`
- Create: `backend/pom.xml`
- Create: `frontend/package.json`
- Create: `database/README.md`
- Create: `docs/项目开发说明.md`
- Create: `.github/workflows/ci.yml`

**Interfaces:**
- Produces: 可分别启动的 `backend` 和 `frontend` 空骨架、统一目录、环境变量示例和 CI 检查入口。

- [ ] 初始化目录并写入 Java 17、Node.js、MySQL 8.0 的版本要求。
- [ ] 配置后端 Maven 编译、测试和 Checkstyle/格式检查所需的最小插件。
- [ ] 配置前端 npm scripts：`dev`、`build`、`lint`。
- [ ] 在 README 写明本地启动顺序，但不提交密码、JWT 密钥或真实环境配置。
- [ ] 运行 `mvn test`、`npm run build`，确认空骨架可通过。
- [ ] 提交 `chore: 初始化项目骨架`。

### Task 2: 数据库结构与基础数据

**Files:**
- Create: `database/01_schema.sql`
- Create: `database/02_seed_data.sql`
- Create: `database/README.md`
- Create: `docs/数据库设计.md`
- Create: `docs/数据库关系说明.md`

**Interfaces:**
- Produces: 数据库 `jiazheng_system`，包含用户、角色、权限、客户、服务人员、分类、项目、区域、地址、需求、工单、分配记录、状态流转、服务过程、图片、评价、投诉处理、公告、登录日志和操作日志表。

- [ ] 为每张表定义主键、创建时间、更新时间、逻辑删除字段和必要索引。
- [ ] 为表、字段、状态枚举和关键外键增加中文注释。
- [ ] 用外键或应用层约束保证工单、评价、投诉和服务记录的关联完整性。
- [ ] 准备不少于 30 客户、20 服务人员、10 服务项目、8 服务区域、100 需求、100 工单、50 调度、100 过程、60 评价、15 投诉和 10 公告。
- [ ] 让种子数据覆盖待审核、待分配、待接单、已接单、服务中、待确认、已完成和已取消等状态。
- [ ] 在干净 MySQL 实例中按顺序执行两个 SQL 文件，并用查询验证关联数量。
- [ ] 提交 `feat: 完成数据库结构和初始化数据`。

### Task 3: 后端基础、认证与权限

**Files:**
- Create: `backend/src/main/java/.../common/ApiResponse.java`
- Create: `backend/src/main/java/.../config/SecurityConfig.java`
- Create: `backend/src/main/java/.../security/JwtAuthenticationFilter.java`
- Create: `backend/src/main/java/.../security/JwtTokenService.java`
- Create: `backend/src/main/java/.../exception/GlobalExceptionHandler.java`
- Create: `backend/src/main/java/.../modules/auth/**`
- Test: `backend/src/test/java/.../security/**`

**Interfaces:**
- Produces: `POST /api/auth/register`、`POST /api/auth/login`、`GET /api/auth/me`、`POST /api/auth/logout`；统一响应结构 `{code,message,data}`；角色编码 `CUSTOMER`、`PROVIDER`、`ADMIN`。

- [ ] 配置 CORS、密码哈希、JWT 有效期和无状态会话。
- [ ] 实现注册时默认客户角色、登录签发令牌和 `/me` 返回当前用户信息。
- [ ] 实现角色注解或权限判断，并在业务层检查数据归属。
- [ ] 对参数错误、令牌失效、无权限和资源不存在返回统一中文错误。
- [ ] 编写登录成功、错误密码、过期令牌和越权访问测试。
- [ ] 提交 `feat: 完成认证和角色权限基础`。

### Task 4: 用户、角色、服务分类、项目和区域管理

**Files:**
- Create: `backend/src/main/java/.../modules/user/**`
- Create: `backend/src/main/java/.../modules/serviceitem/**`
- Create: `backend/src/main/java/.../modules/region/**`
- Test: `backend/src/test/java/.../modules/**`

**Interfaces:**
- Produces: 管理员 CRUD 接口、客户个人资料接口、服务人员档案/技能/工作状态接口、服务分类/项目/区域接口，返回分页对象。

- [ ] 统一分页、排序、关键字查询和状态筛选参数。
- [ ] 对服务项目的启用/停用、人员工作状态和区域归属做后端校验。
- [ ] 对管理员操作写入操作日志。
- [ ] 测试管理员可管理、客户只可读取授权数据、停用项目不能新建需求。
- [ ] 提交 `feat: 完成基础资料和服务项目管理`。

### Task 5: 客户地址、服务需求与工单状态机

**Files:**
- Create: `backend/src/main/java/.../modules/demand/**`
- Create: `backend/src/main/java/.../modules/workorder/WorkOrderStateMachine.java`
- Create: `backend/src/main/java/.../modules/workorder/**`
- Test: `backend/src/test/java/.../modules/workorder/WorkOrderStateMachineTest.java`

**Interfaces:**
- Produces: 客户需求 CRUD、管理员审核、工单生成、工单查询/取消/详情；状态转换入口 `transition(workOrderId, targetStatus, operatorId, reason)`。

- [ ] 实现需求校验：服务项目启用、预约时间合法、地址属于当前客户。
- [ ] 审核通过时在事务中创建工单并写入初始状态流转记录。
- [ ] 明确定义允许的状态图：待审核→待分配→待接单→已接单→服务中→待确认→已完成/已取消，并保留异常和改派分支。
- [ ] 禁止通过普通更新接口直接篡改状态，所有状态变更经过状态机。
- [ ] 测试正常流转、非法跳转、取消规则和状态记录完整性。
- [ ] 提交 `feat: 完成需求审核和工单状态流转`。

### Task 6: 调度、接单与服务过程追踪

**Files:**
- Create: `backend/src/main/java/.../modules/scheduling/**`
- Create: `backend/src/main/java/.../modules/serviceprocess/**`
- Test: `backend/src/test/java/.../modules/scheduling/**`

**Interfaces:**
- Produces: 候选人员查询、手动派单、转派、接单/拒单、服务过程记录、服务图片元数据接口；调度规则按技能、区域、可用状态、时间冲突和当前负载返回可解释结果。

- [ ] 实现候选人员筛选，并返回不满足条件的原因。
- [ ] 派单和转派使用事务，防止同一人员在同一时间段重复占用。
- [ ] 服务人员只能操作分配给自己的工单。
- [ ] 记录前往、到达、开始、进行中、完成等过程节点和操作人。
- [ ] 图片只保存安全的文件标识/路径元数据，不把真实个人隐私写入仓库。
- [ ] 测试重复分配、拒单改派、越权操作和过程时间线。
- [ ] 提交 `feat: 完成人员调度和服务过程追踪`。

### Task 7: 验收评价、投诉返工与质量统计接口

**Files:**
- Create: `backend/src/main/java/.../modules/review/**`
- Create: `backend/src/main/java/.../modules/complaint/**`
- Create: `backend/src/main/java/.../modules/statistics/**`
- Test: `backend/src/test/java/.../modules/statistics/**`

**Interfaces:**
- Produces: 客户验收/评价、投诉创建/处理/回访/关闭、管理员质量监督和 ECharts 所需统计接口。

- [ ] 只有待确认工单允许验收，只有验收后的工单允许评价。
- [ ] 评价包含评分、态度、质量、及时性和文字内容，统计服务人员平均分与完成次数。
- [ ] 投诉必须关联原工单，返工或改派必须留下处理记录。
- [ ] 统计接口支持日期、服务项目、人员和区域筛选，并返回零值/空数组而不是空指针。
- [ ] 测试评价重复提交、投诉关闭条件和空数据统计。
- [ ] 提交 `feat: 完成质量评价投诉和统计接口`。

### Task 8: 前端布局、登录和权限菜单

**Files:**
- Create: `frontend/src/router/**`
- Create: `frontend/src/stores/auth.js`
- Create: `frontend/src/api/request.js`
- Create: `frontend/src/layouts/**`
- Create: `frontend/src/views/login/**`
- Create: `frontend/src/components/**`

**Interfaces:**
- Consumes: Task 3 的认证接口和当前用户结构。
- Produces: 登录页、后台布局、角色菜单、路由守卫、请求拦截器和中文错误提示。

- [ ] 实现登录、退出、令牌持久化和失效跳转。
- [ ] 根据角色返回菜单并在前端隐藏无权限操作，但不把前端隐藏当作安全边界。
- [ ] 配置全局表格、分页、确认框和错误提示风格。
- [ ] 测试三种角色登录后菜单差异和令牌失效行为。
- [ ] 提交 `feat: 完成前端基础布局和登录权限`。

### Task 9: 前端基础资料、需求、工单和调度页面

**Files:**
- Create: `frontend/src/views/dashboard/**`
- Create: `frontend/src/views/customer/**`
- Create: `frontend/src/views/provider/**`
- Create: `frontend/src/views/service-item/**`
- Create: `frontend/src/views/demand/**`
- Create: `frontend/src/views/work-order/**`
- Create: `frontend/src/views/scheduling/**`

**Interfaces:**
- Consumes: Tasks 4–6 的分页、详情、状态操作和调度接口。
- Produces: 中文表格、查询筛选、分页、详情抽屉/页面、状态操作和工单时间线。

- [ ] 客户完成服务项目浏览、地址管理和需求提交。
- [ ] 管理员完成项目、区域、用户、需求和工单管理。
- [ ] 服务人员完成待办工单、接单/拒单和服务过程记录。
- [ ] 任何列表都从后端加载，不使用业务静态数据。
- [ ] 测试核心页面在真实种子数据下可以完成一次完整流程。
- [ ] 提交 `feat: 完成核心业务前端页面`。

### Task 10: 前端质量追踪、投诉、公告、日志和看板

**Files:**
- Create: `frontend/src/views/review/**`
- Create: `frontend/src/views/complaint/**`
- Create: `frontend/src/views/quality/**`
- Create: `frontend/src/views/notice/**`
- Create: `frontend/src/views/log/**`
- Modify: `frontend/src/views/dashboard/**`

**Interfaces:**
- Consumes: Task 7 的评价、投诉和统计接口。
- Produces: ECharts 真实统计、投诉处理页面、公告页面、操作/登录日志页面和质量监督页面。

- [ ] 实现工单趋势、状态分布、服务项目占比、人员工作量、评分排行和投诉处理统计。
- [ ] 对日期、项目、人员和区域筛选后重新请求后端数据。
- [ ] 空数据、接口超时和统计失败均有中文提示。
- [ ] 测试图表数据与数据库查询结果一致。
- [ ] 提交 `feat: 完成质量监督和统计看板`。

### Task 11: 联调、测试、文档和答辩材料

**Files:**
- Create: `docs/项目需求分析.md`
- Create: `docs/系统功能设计.md`
- Create: `docs/系统架构设计.md`
- Create: `docs/接口文档.md`
- Create: `docs/前端页面说明.md`
- Create: `docs/环境部署说明.md`
- Create: `docs/系统测试说明.md`
- Create: `backend/src/test/**`
- Create: `frontend/src/**/__tests__/**` (如项目采用组件测试)
- Modify: `README.md`

**Interfaces:**
- Consumes: Tasks 1–10 的运行项目和接口。
- Produces: 可复现的启动说明、完整测试记录、接口文档、业务演示脚本和截图目录。

- [ ] 在干净环境按 README 初始化数据库、启动后端和前端。
- [ ] 执行客户注册→需求→审核→派单→接单→执行→完成→确认→评价→投诉→统计全流程。
- [ ] 执行角色越权、非法状态、重复调度、空统计和异常参数测试。
- [ ] 检查文档与代码实际接口、字段、端口和账号一致。
- [ ] 删除临时文件、密钥、真实隐私和硬编码生产配置。
- [ ] 提交 `test: 完成全流程联调和验收测试` 与 `docs: 完善项目文档`。

### Task 12: GitHub 发布前检查

**Files:**
- Modify: `.github/workflows/ci.yml`
- Modify: `README.md`

**Interfaces:**
- Consumes: Task 11 的测试和文档成果。
- Produces: 远程仓库可审阅的完整项目、可通过的 CI 和清晰的版本历史。

- [ ] 检查 `git status`，确认没有未说明的临时文件和敏感信息。
- [ ] 运行后端测试、前端构建和 SQL 干净导入检查。
- [ ] 检查 README 中的仓库地址、启动命令、默认账号和限制说明。
- [ ] 仅在确认远程写权限后推送；没有权限时保留本地提交并明确说明。
- [ ] 创建最终提交 `chore: 完成本科毕业设计项目交付`。

## 阶段验收表

| 阶段 | 完成标志 | 可演示内容 |
|---|---|---|
| 1 项目基线 | 前后端骨架、README、CI | 项目可安装、可编译 |
| 2 数据库 | SQL 可执行、种子数据关联正确 | MySQL 导入后查询到完整业务数据 |
| 3 后端基础 | 登录、JWT、RBAC、统一异常 | 三角色登录与越权拦截 |
| 4 核心业务 | 需求、工单、调度、服务过程 | 一条工单完整流转 |
| 5 质量闭环 | 验收、评价、投诉、统计 | 评价和投诉反映到看板 |
| 6 前端交付 | 所有主要页面真实对接 | 浏览器端完成闭环 |
| 7 文档验收 | 文档与代码一致 | 按 README 可复现启动 |
| 8 GitHub 发布 | 检查通过并提交 | 仓库可供导师审阅 |

## 现状检查结论

当前远程仓库 `https://github.com/xiaofeng-cloud/jiazheng-system` 可访问，但仓库为空，没有可用提交、分支内容、前后端代码、数据库脚本、文档或 CI 配置。因此执行时应从 Task 1 开始，不能按“接续已有项目”假设文件结构或接口已经存在。

