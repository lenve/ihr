# docker

docker compose 编排目录：MySQL 8 / Redis 7 / RabbitMQ 3.12。

```bash
docker compose up -d        # 一键启动
docker compose ps           # 查看状态
docker compose down         # 停止（保留数据卷）
docker compose down -v      # 停止并清除数据（会重置 MySQL 初始化数据）
```

- MySQL 初始化脚本挂载：`mysql/init/` → `/docker-entrypoint-initdb.d`（仅首次建卷时执行）
- 端口映射见根目录 README「端口与账号一览」
- RabbitMQ 管理控制台：<http://localhost:15673>（ihr / rabbit123）
