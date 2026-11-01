# 任务：搭建 iHR 智慧人力资源管理系统（仅框架，不实现业务）

## 一、技术栈
- 网页端：Vue 3 + TypeScript + Vite + Vue Router + Pinia + Element Plus
- 后端：Spring Boot 3.x + JDK 17 + Maven + MyBatis-Plus，鉴权用 Spring Security + JWT
- 小程序端：uni-app（Vue3 语法，编译目标为微信小程序）
- 数据库：MySQL 8（Docker 容器运行）
- 缓存：Redis 7（Docker 容器运行）
- 消息中间件：RabbitMQ（Docker 容器运行）
- 提供 docker-compose.yml 一键编排 MySQL、Redis、RabbitMQ（含初始化脚本挂载、端口映射、数据卷持久化）

## 二、项目结构（Monorepo）
ihr/
├── ihr-web/          # 网页端
├── ihr-server/       # 后端（按 controller/service/mapper/entity/dto/vo/config/common 分包，预留 system、recruit、performance、talent、salary 等业务模块骨架）
├── ihr-miniprogram/  # 小程序端
├── docker/           # docker-compose.yml、MySQL 初始化 SQL
└── docs/             # README 与接口约定

## 三、菜单结构（网页端侧边栏按此分组，可自行微调分组）
1. 个人工作台：个人信息、待办通知、工作任务、预警通知
2. 招聘与人才：招聘信息、人才管理、人才申报、人力资源池
3. 绩效考核：考核表评分、考核关系、我的业绩、团队业绩申请、
   我的绩效（二级：目标制定、员工自评、上级评价、签署进度、面谈填写、
   面谈审核、绩效考核结果、竞争考核结果、我的申诉、申诉审核、我的下属）
4. 人事服务：人事报表、通知公告、政策解读、工资查询、办事指南、
   培训档案、出入境指南、晋升提拔公示、车辆违规查询
5. 申报与审批：签证申请、职称申报、我的职称
6. 平台功能：信息收发平台

## 四、本次交付范围（只搭框架，不实现具体业务逻辑）
- 网页端：登录页占位、主布局（侧边菜单+顶栏）、完整路由与菜单配置，
  每个菜单对应一个占位页面（页面标题+空容器即可）
- 小程序端：项目初始化、首页 TabBar 框架、核心页面占位
- 后端：统一返回结构、全局异常处理、JWT 登录接口骨架（可用假数据）、
  健康检查接口、按业务模块建好空 Controller/Service 骨架
- 数据库：建库脚本 + 基础表（用户、角色、菜单、用户-角色、角色-菜单）及初始化数据

## 五、验收标准
1. docker-compose up -d 一键启动 MySQL/Redis/RabbitMQ
2. 后端可启动，登录接口与健康检查可调通
3. 网页端可启动，登录后能看到完整分组菜单，各页面为占位页
4. README 包含完整的本地启动步骤（端口、账号密码、启动顺序）
5. 接口文档（Swagger）可访问
6. 代码结构清晰，符合规范