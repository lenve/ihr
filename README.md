# iHR 智慧人力资源管理系统

网页端 + 后端 + 小程序端 + 基础设施一键编排的 HR 管理系统。网页端 **36 个菜单页面均已实现具体业务**（个人工作台、招聘与人才、绩效考核、人事服务、申报与审批、信息收发平台），后端提供 7 大业务模块的完整 REST 接口与演示数据。

## 一、技术栈

| 端 | 技术 |
| --- | --- |
| 网页端 | Vue 3 + TypeScript + Vite + Vue Router + Pinia + Element Plus + Axios |
| 后端 | Spring Boot 3.5 + JDK 17（JDK 24 可运行）+ Maven + MyBatis-Plus + Lombok + Spring Security + JWT + springdoc-openapi |
| 小程序端 | uni-app（Vue3 语法，编译目标微信小程序，框架阶段） |
| 数据库 | MySQL 8（Docker） |
| 缓存 | Redis 7（Docker） |
| 消息中间件 | RabbitMQ 3.12（Docker） |

## 二、目录结构

```
ihr/
├── ihr-web/          # 网页端（36 个业务页面 + CrudPage 通用增删改查组件）
├── ihr-server/       # 后端（system/workbench/recruit/talent/performance/hrservice/declare/platform 模块）
├── ihr-miniprogram/  # 小程序端（TabBar 框架 + 登录）
├── docker/           # docker-compose.yml + MySQL 初始化 SQL（01 系统表 / 02 业务表 + 演示数据）
└── docs/             # 接口约定文档
```

后端分包：`common`（Result/全局异常/安全工具）、`config`（Security/MyBatis-Plus/RabbitMQ/OpenAPI）、`security`（JWT 三件套）、`module/*`（业务模块，每个模块固定 entity/mapper/service/controller 结构）。

## 三、功能清单（网页端 36 页）

| 菜单组 | 功能 |
| --- | --- |
| 个人工作台 | 个人信息（档案查看编辑）、待办通知（完成标记）、工作任务（进度/派发）、预警通知（分级处理） |
| 招聘与人才 | 招聘信息（职位发布）、人才管理（人才库评级）、人才申报（推荐审核）、人力资源池（内外部占用） |
| 绩效考核 | 考核表评分、考核关系、我的业绩、团队业绩申请，以及"我的绩效"11 个子流程：目标制定、员工自评、上级评价、签署进度、面谈填写、面谈审核、绩效考核结果、竞争考核结果（排名）、我的申诉、申诉审核、我的下属 |
| 人事服务 | 人事报表（统计卡片）、通知公告、政策解读、工资查询（本人/全员）、办事指南、培训档案、出入境指南、晋升提拔公示、车辆违规查询 |
| 申报与审批 | 签证申请（审批流转）、职称申报（审核）、我的职称 |
| 平台功能 | 信息收发平台（收发件箱、未读数、写信） |

### 绩效考核闭环（核心业务）

状态机：`目标制定 → 目标确认 → 自评中 → 上级评价 → 待面谈 → 面谈审核 → 待签署 → 已完成`，支持 `申诉中` 分支。每条目标带权重，自评总分与上级总分按权重加权汇总，综合得分 = 自评 × 40% + 上级 × 60%，等级映射 ≥90 S / ≥80 A / ≥70 B / ≥60 C / 其余 D。所有流转动作带状态校验，非法流转返回业务错误。

## 四、端口与账号

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

> 登录用户与员工档案关联：admin→系统管理员、zhangsan→张三（技术部，有下属考核待评价）、lisi→李四。业务页面按中文姓名关联数据。

## 五、本地启动步骤（按顺序）

### 1. 启动基础设施

```bash
cd docker
docker compose up -d
# 首次启动自动执行 mysql/init/01_init.sql（5 张系统表）+ 02_business.sql（24 张业务表 + 演示数据）
docker compose ps   # 三个容器 healthy 即就绪
```

### 2. 启动后端（ihr-server）

需要 JDK 17+ 与 Maven（或直接用 IDE 打开运行 `IhrApplication`）：

```bash
cd ihr-server
mvn spring-boot:run
# 或：mvn package -DskipTests && java -jar target/ihr-server-0.0.1-SNAPSHOT.jar
```

验证：

```bash
curl http://localhost:8090/api/health
curl -X POST http://localhost:8090/api/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"username":"zhangsan","password":"123456"}'
```

Swagger 接口文档：<http://localhost:8090/swagger-ui.html>（含全局 Bearer 鉴权按钮，登录后将 token 粘贴到 Authorize 即可调试全部业务接口）

### 3. 启动网页端（ihr-web）

```bash
cd ihr-web
npm install
npm run dev     # http://localhost:5173
```

浏览器访问 <http://localhost:5173> → 登录 `zhangsan / 123456` → 侧边栏可见完整分组菜单。
推荐体验路径：**待办通知**（有待办）→ **我的绩效/员工自评**（填分提交）→ **绩效考核/考核表评分**（为下属李四打分，观察总分与等级计算）→ **人事服务/人事报表**（统计汇总）→ **平台功能/信息收发平台**（收发消息）。

### 4. 启动小程序端（ihr-miniprogram）

```bash
cd ihr-miniprogram
npm install
npm run dev:mp-weixin     # 开发模式，产物在 dist/dev/mp-weixin
```

用微信开发者工具「导入项目」选择 `dist/dev/mp-weixin` 目录；TabBar 三个页签：首页 / 待办 / 我的；登录页可调通后端登录接口（manifest 已默认 `urlCheck: false`）。

## 六、接口约定

所有接口返回统一 `Result<T>`：

```json
{ "code": 0, "message": "成功", "data": { } }
```

- `code=0` 成功；400 参数错误、401 未登录、403 无权限、1000 业务失败、1001 账密错误（完整错误码见 `docs/api.md`）
- 鉴权：登录返回 JWT，后续请求携带 `Authorization: Bearer <token>`；白名单仅登录、健康检查与 Swagger
- 业务模块均为标准 REST：`GET 分页（pageNum/pageSize/keyword/过滤字段）`、`POST` 新增、`PUT /{id}` 修改、`DELETE /{id}` 删除，外加流程动作（如 `PUT /api/performance/appraisals/{id}/sign`）

主要业务接口前缀：

| 模块 | 前缀 |
| --- | --- |
| 个人工作台 | `/api/workbench/profiles·todos·tasks·alerts` |
| 招聘与人才 | `/api/recruit/positions`、`/api/talent/profiles·declarations·pools` |
| 绩效考核 | `/api/performance/appraisals·goals·interviews·appeals·achievements·relations` |
| 人事服务 | `/api/hrservice/notices·articles·salaries·trainings·promotions·vehicle-violations·report` |
| 申报与审批 | `/api/declare/visas·titles·title-records` |
| 平台功能 | `/api/platform/messages` |

## 七、核心设计说明

- **统一请求链路**：后端 `Result<T>` + 全局异常兜底；网页端 axios 封装自动解包、注入 Bearer、401 跳登录
- **CrudPage 通用组件**：网页端列表页的列/表单/搜索/行操作全配置化，一个组件覆盖大多数增删改查场景；需要抽屉查看、tabs、统计等能力时写自定义页面
- **路由即菜单**：侧边栏由 `ihr-web/src/router/index.ts` 路由树的 `meta.title/icon` 驱动，无需单独菜单配置
- **当前用户身份**：JWT 携带 username，`EmployeeService.currentRealName()` 按员工档案映射为中文姓名，业务数据以姓名关联
- **审计字段**：全部表 `create_time/update_time` 由数据库 `DEFAULT CURRENT_TIMESTAMP / ON UPDATE` 维护，应用层不写入

## 八、已知限制与后续演进

- 登录为假数据（`FakeUserRepository`），接入真实业务改为查询 `sys_user` + BCrypt 校验（`PasswordEncoder` 已就绪）
- 无 RBAC 角色权限区分（如工资"查看全员"开关未鉴权），可在 `SecurityConfig` 上叠加方法级权限
- 面谈记录暂无页面新增入口（可经 Swagger 或后续补充）
- 菜单为静态路由，动态菜单可切换 `GET /api/system/menus`（接口已提供）
- `RabbitMqConfig` 已声明示例队列 `ihr.todo.notify.queue`，可扩展待办/预警推送
