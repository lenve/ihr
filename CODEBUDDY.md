# CODEBUDDY.md This file provides guidance to CodeBuddy when working with code in this repository.

# iHR 智慧人力资源管理系统

单体仓库：`ihr-web`（网页端）、`ihr-server`（后端）、`ihr-miniprogram`（微信小程序端）、`docker/`（MySQL/Redis/RabbitMQ 编排）、`docs/`（接口约定 `docs/api.md`）。36 个菜单页均已实现具体业务（2026-11 起），登录仍为假数据（`FakeUserRepository`，admin/zhangsan/lisi，密码 123456），小程序端仍为框架。

## 常用命令

### 基础设施（先启动）

```bash
cd docker && docker compose up -d   # MySQL 13306 / Redis 16379 / RabbitMQ 56772(AMQP)+15673(控制台)
docker compose ps                   # 三个容器 healthy 即就绪
docker compose down -v              # 重置（重跑 mysql/init/01_init.sql + 02_business.sql）
```

MySQL 初始化：`01_init.sql`（sys_user 等 5 张系统表）+ `02_business.sql`（24 张业务表 + 演示数据），仅首次建卷执行；改 SQL 需 `down -v` 重建。对运行中的库执行增量 SQL：`docker exec -i ihr-mysql mysql -uroot -proot123 ihr < xxx.sql`。

### 后端（ihr-server，Spring Boot 3.5 + JDK 17 源码级/JDK 24 运行 + Maven）

```bash
cd ihr-server
mvn spring-boot:run                                                  # 端口 8090
mvn package -DskipTests                                             # 打包
mvn test -Dtest=ClassName                                           # 单个测试类（当前无测试）
```

本机无全局 `mvn`，使用 Maven Wrapper 分发版：`~/.m2/wrapper/dists/apache-maven-3.9.11-bin/6mqf5t809d9geo83kj4ttckcbc/apache-maven-3.9.11/bin/mvn`，需 `export JAVA_HOME=/Users/javaboy/Library/Java/JavaVirtualMachines/openjdk-24.0.1/Contents/Home`。验证：`curl http://localhost:8090/api/health`；Swagger：http://localhost:8090/swagger-ui.html 。

### 网页端（ihr-web，Vue 3 + TS + Vite）

```bash
cd ihr-web
npm run dev      # http://localhost:5173，/api 代理到 8090
npm run build    # 构建验证（vite 不做 TS 类型检查）
```

无 lint/test 脚本。

### 小程序端

```bash
cd ihr-miniprogram && npm run dev:mp-weixin   # 产物 dist/dev/mp-weixin，微信开发者工具导入
```

## 架构

### 鉴权与请求链路

- 后端统一返回 `Result<T>`（`common/result/`，`code=0` 成功，错误码见 `docs/api.md`）；全局异常 `common/exception/GlobalExceptionHandler`；业务错误抛 `BusinessException`。
- 无状态 JWT：`security/JwtTokenProvider` + `JwtAuthenticationFilter`；白名单仅登录/健康检查/Swagger。当前登录用户名：`SecurityUtils.currentUsername()`；当前用户**姓名**（业务数据用中文姓名关联）：`module/workbench/service/EmployeeService.currentRealName()`（按 `emp_employee.username` 映射 `real_name`，无档案回退用户名）。
- 前端请求层都按 `code !== 0` 判失败并解包 `data`：网页端 `utils/request.ts`（axios，`baseURL:'/api'`，401 清 token 跳登录）；小程序端 `src/utils/request.ts`。新增调用必须走封装。

### 后端业务分包（com.ihr.module）

- `workbench/`：员工档案（Employee + `/api/workbench/profile`）、待办 todo_item、工作任务 work_task、预警 alert_notice
- `recruit/`（招聘职位）、`talent/`（人才库/人才申报/人力资源池）
- `performance/`：绩效六表（relation/appraisal/goal/interview/appeal/achievement），**状态机**见下
- `hrservice/`：通知公告、内容文章（政策/办事/出入境三类共用 cms_article，按 category 区分）、工资、培训、晋升公示、车辆违规、人事报表聚合（`/api/hrservice/report/overview`）
- `declare/`（签证/职称申报/职称记录）、`platform/`（站内消息 `/api/platform/messages`）

模块内固定结构：`entity/`（Lombok `@Data` + `@TableName`）、`mapper/`（`@Mapper extends BaseMapper`）、`service/` + `service/impl/`（`ServiceImpl` + LambdaQueryWrapper）、`controller/`（构造器注入、`Result.ok()`、`@Tag/@Operation`）。标准 REST：GET 分页（`pageNum/pageSize/keyword/过滤字段`）→ `IPage`、POST 新增、PUT/DELETE `/{id}`；流程动作为额外 `PUT /{id}/xxx`。**注意**：Lombok 需 pom 中 maven-compiler-plugin 的 `annotationProcessorPaths` 显式声明（JDK 23+ 禁用 classpath 隐式注解处理）；实体 createTime/updateTime 由数据库维护，不要在代码中 set。

### 绩效状态机（perf_appraisal.status）

`0目标制定 → 1目标确认 → 2自评中 → 3上级评价 → 4待面谈 → 5面谈审核 → 6待签署 → 7已完成`，`8申诉中`。流转动作在 `PerfAppraisalController`/`PerfInterviewController`/`PerfAppealController`，均带状态校验（非法流转抛 BusinessException）。评分模型：每条目标有权重，自评总分=Σ(self×weight)/100、上级总分=Σ(superior×weight)/100、综合=自评 40%+上级 60%；等级 ≥90 S / ≥80 A / ≥70 B / ≥60 C / 否则 D。

### 前端结构

- **路由即菜单**：`router/index.ts` 路由树驱动侧边栏（`meta.title/icon/hidden`），叶子路由经 `view(group, name)` 动态导入 `views/<分组>/<页面>.vue`。
- **`components/CrudPage.vue`**：通用增删改查组件（列/表单/搜索/行操作全配置化，接口约定与后端标准 REST 对齐）。简单列表页优先复用它；需要抽屉查看、tabs、统计图表等能力时写自定义页面（参考 `hr-service/Notice.vue`、`HrReport.vue`、`platform/Message.vue`）。绩效模块有 `views/performance/perf-shared.ts`（状态/等级字典）与 `components/ScoreDialog.vue`、`AchievementPage.vue` 两个复用组件。
- 当前用户昵称：`localStorage ihr_user` 的 `nickname` 或 `useUserStore().displayName()`。
- 不新增 npm 依赖（统计用 el-card/el-progress 呈现，未引入图表库）。

### 数据库

29 张表：5 张系统表（`01_init.sql`）+ 24 张业务表（`02_business.sql`，表结构含中文注释与状态枚举）。MyBatis-Plus 驼峰映射已开启，分页插件已配置。

### 端口约定（均避开本机常用端口）

后端 8090、网页端 5173、MySQL 13306、Redis 16379、RabbitMQ 56772/15673。JDBC URL 用 `characterEncoding=UTF-8`（不要写 utf8mb4，Java 不识别该字符集名，Connector/J 会自动映射）。

### 已知过时/遗留内容（勿被误导）

- 根目录 `README.md` 部分描述停留在框架阶段（"仅框架、不含业务逻辑"、"所有页面均为占位页"），`docs/api.md` 只列了框架期接口——业务接口以 Swagger（/swagger-ui.html）和各 Controller 为准。
- 框架期探活骨架与新业务 Controller 并存：`RecruitController`(/api/recruit/ping)、`TalentController`(/api/talent/ping)、`SalaryController`(/api/salary/ping)、`PerformanceController`(/api/performance/ping) 是旧骨架，真正的业务接口在同包的 `RecruitPositionController`、`TalentProfileController` 等；骨架可安全删除。
- `ihr-web/src/views/PlaceholderView.vue` 已不被路由引用（历史遗留）。
