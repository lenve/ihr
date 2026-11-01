# iHR 智慧人力资源管理系统（框架版）

> 仅框架、不含业务逻辑：网页端 + 后端 + 小程序端 + 基础设施一键编排。

## 一、技术栈

| 端 | 技术 |
| --- | --- |
| 网页端 | Vue 3 + TypeScript + Vite + Vue Router + Pinia + Element Plus |
| 后端 | Spring Boot 3.5 + JDK 17 + Maven + MyBatis-Plus + Spring Security + JWT + springdoc-openapi |
| 小程序端 | uni-app（Vue3 语法，编译目标微信小程序） |
| 数据库 | MySQL 8（Docker） |
| 缓存 | Redis 7（Docker） |
| 消息中间件 | RabbitMQ 3.12（Docker） |

## 二、目录结构

```
ihr/
├── ihr-web/          # 网页端（登录页 + 主布局 + 完整路由/菜单 + 占位页）
├── ihr-server/       # 后端（统一返回/全局异常/JWT 登录/健康检查/Swagger + 业务模块骨架）
├── ihr-miniprogram/  # 小程序端（TabBar 框架 + 核心页面占位）
├── docker/           # docker-compose.yml + MySQL 初始化 SQL
└── docs/             # 接口约定文档
```

后端分包：`common`（Result/异常/健康检查）、`config`（Security/MyBatis-Plus/RabbitMQ/OpenAPI）、
`security`（JWT 工具与过滤器）、`module`（system、recruit、performance、talent、salary 模块骨架）。

## 三、端口与账号一览

### 端口（宿主机）

| 服务 | 端口 | 说明 |
| --- | --- | --- |
| MySQL | **13306** | 本机 3306 被占用，改用 13306 映射容器 3306 |
| Redis | **16379** | 本机 6379 被占用 |
| RabbitMQ AMQP | **56772** | 本机 5672 被占用 |
| RabbitMQ 管理台 | **15673** | 本机 15672 被占用 |
| 后端 ihr-server | **8090** | 本机 8080 被占用 |
| 网页端 dev | **5173** | /api 代理到 8090 |

> 若本机端口无冲突，可在 `docker/docker-compose.yml` 与 `ihr-server/src/main/resources/application.yml` 中改回默认端口。

### 账号密码

| 用途 | 账号 | 密码 |
| --- | --- | --- |
| 系统登录（假数据） | admin / zhangsan / lisi | 123456 |
| MySQL root | root | root123 |
| Redis | - | redis123 |
| RabbitMQ 控制台 | ihr | rabbit123 |

## 四、本地启动步骤（按顺序）

### 1. 启动基础设施

```bash
cd ihr/docker
docker compose up -d
# 首次启动会自动执行 mysql/init/01_init.sql：建库 ihr + 5 张基础表 + 初始化数据
docker compose ps   # 三个容器 healthy 即就绪
```

### 2. 启动后端（ihr-server）

需要 JDK 17+ 与 Maven（或直接用 IDE 打开运行 `IhrApplication`）：

```bash
cd ihr/ihr-server
mvn spring-boot:run
# 或：mvn package -DskipTests && java -jar target/ihr-server-0.0.1-SNAPSHOT.jar
```

验证：

```bash
curl http://localhost:8090/api/health
curl -X POST http://localhost:8090/api/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"username":"admin","password":"123456"}'
```

Swagger 接口文档：<http://localhost:8090/swagger-ui.html>

### 3. 启动网页端（ihr-web）

```bash
cd ihr/ihr-web
npm install
npm run dev     # http://localhost:5173
```

浏览器访问 <http://localhost:5173> → 登录页输入 `admin / 123456` → 进入主布局，
侧边栏可见完整分组菜单（个人工作台 / 招聘与人才 / 绩效考核 / 人事服务 / 申报与审批 / 平台功能），
所有页面均为占位页（页面标题 + 空容器）。

### 4. 启动小程序端（ihr-miniprogram）

```bash
cd ihr/ihr-miniprogram
npm install
npm run dev:mp-weixin     # 开发模式，产物在 dist/dev/mp-weixin
# 或一次性编译：npm run build:mp-weixin（产物在 dist/build/mp-weixin）
```

用微信开发者工具「导入项目」选择 `dist/dev/mp-weixin`（或 build 产物）目录。
TabBar 三个页签：首页 / 待办 / 我的；登录页可调通后端登录接口
（需在开发者工具中保持「不校验合法域名」，manifest 已默认 `urlCheck: false`）。

## 五、验收对照

| 验收项 | 状态 |
| --- | --- |
| docker compose 一键启动 MySQL/Redis/RabbitMQ | ✅ 含初始化脚本挂载、端口映射、数据卷持久化、健康检查 |
| 后端可启动，登录与健康检查可调通 | ✅ 统一返回 Result + 全局异常 + JWT 假数据登录 + /api/health |
| 网页端登录后可见完整分组菜单，各页为占位页 | ✅ 36 个叶子菜单全部占位（含"我的绩效"二级 11 项） |
| README 完整启动步骤（端口/账号/顺序） | ✅ 见上文 |
| Swagger 可访问 | ✅ /swagger-ui.html，全局 Bearer 鉴权按钮 |
| 数据库基础表 + 初始化数据 | ✅ 用户/角色/菜单/用户-角色/角色-菜单 5 张表 |

## 六、后续业务接入提示

- 登录：`AuthServiceImpl` 目前走假数据，接入真实业务时改为查询 `sys_user` + BCrypt 校验（`PasswordEncoder` 已就绪）。
- 菜单：网页端菜单当前由 `ihr-web/src/router/index.ts` 路由配置驱动；动态菜单可改用 `GET /api/system/menus`（已提供菜单树接口）。
- 业务模块：`module/recruit|performance|talent|salary` 下均为空 Controller/Service 骨架，按 TODO 注释扩展。
- 消息：`RabbitMqConfig` 已声明 `ihr.todo.notify.queue` 示例队列，可扩展待办/预警推送。
