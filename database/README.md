# 数据库目录

数据库名称为 `jiazheng_system`，字符集使用 `utf8mb4`，时区使用 `Asia/Shanghai`。

按以下顺序执行数据库脚本：

- `01_schema.sql`：数据库、表、索引和中文注释
- `02_seed_data.sql`：虚构的中国大陆测试数据

测试数据只用于本地开发和毕业设计演示，不包含真实个人信息。

```bash
mysql -u root -p < database/01_schema.sql
mysql -u root -p < database/02_seed_data.sql
```

演示账号为 `admin`、`customer01`、`provider01`，密码均为 `123456`（脚本中只保存 BCrypt 摘要）。数据库不包含支付、交易、充值、退款、押金或结算功能。
