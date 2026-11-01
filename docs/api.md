# iHR 接口约定文档

## 1. 基础信息

- 基础地址：`http://localhost:8090`
- 网页端经 Vite 代理访问：`http://localhost:5173/api/*` → `http://localhost:8090/api/*`
- Swagger UI：`http://localhost:8090/swagger-ui.html`
- OpenAPI JSON：`http://localhost:8090/v3/api-docs`

## 2. 统一返回结构

所有接口返回 `Result<T>`：

```json
{
  "code": 0,
  "message": "成功",
  "data": { }
}
```

| code | 含义 |
| --- | --- |
| 0 | 成功 |
| 400 | 请求参数错误（含 @Valid 校验失败） |
| 401 | 未登录或 token 过期（HTTP 状态同为 401） |
| 403 | 无访问权限 |
| 404 | 资源/接口不存在 |
| 500 | 系统内部错误 |
| 1000 | 业务处理失败 |
| 1001 | 用户名或密码错误 |

## 3. 鉴权约定

- 登录成功返回 JWT；后续请求携带请求头 `Authorization: Bearer <token>`。
- token 有效期 8 小时（`jwt.expire` 可配置），签名密钥 `jwt.secret`（生产环境务必替换）。
- 白名单（无需登录）：`POST /api/auth/login`、`GET /api/health`、Swagger 相关路径。
- 其余接口一律需要登录，未携带/无效 token 返回 401 Result 结构。

## 4. 已提供接口

| 方法 | 路径 | 说明 | 鉴权 |
| --- | --- | --- | --- |
| POST | `/api/auth/login` | 登录（假数据：admin/123456、zhangsan/123456、lisi/123456） | 否 |
| GET | `/api/auth/me` | 当前登录用户信息 | 是 |
| POST | `/api/auth/logout` | 登出（JWT 无状态，客户端删 token） | 是 |
| GET | `/api/health` | 服务健康检查 | 否 |
| GET | `/api/system/users?pageNum=&pageSize=` | 用户分页（MyBatis-Plus 骨架，需 MySQL 运行） | 是 |
| GET | `/api/system/menus` | 菜单树（sys_menu 表构建，供动态菜单演进） | 是 |
| GET | `/api/recruit/ping` | 招聘与人才模块探活（骨架） | 是 |
| GET | `/api/performance/ping` | 绩效考核模块探活（骨架） | 是 |
| GET | `/api/talent/ping` | 人才模块探活（骨架） | 是 |
| GET | `/api/salary/ping` | 薪酬模块探活（骨架） | 是 |

### 登录请求/响应示例

请求：

```json
POST /api/auth/login
{ "username": "admin", "password": "123456" }
```

响应：

```json
{
  "code": 0,
  "message": "成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "expiresIn": 28800000,
    "user": {
      "id": 1,
      "username": "admin",
      "nickname": "系统管理员",
      "email": "admin@ihr.com",
      "phone": "13800000000",
      "roles": ["ADMIN"]
    }
  }
}
```

## 5. 数据库基础表

| 表 | 说明 |
| --- | --- |
| `sys_user` | 用户（初始化 3 人） |
| `sys_role` | 角色（ADMIN / EMPLOYEE / HR） |
| `sys_menu` | 菜单（顶级分组与网页端侧边栏一致） |
| `sys_user_role` | 用户-角色关联 |
| `sys_role_menu` | 角色-菜单关联 |

建库/初始化脚本：`docker/mysql/init/01_init.sql`（首次 `docker compose up -d` 自动执行）。

## 6. 小程序端约定

- 请求封装：`ihr-miniprogram/src/utils/request.ts`（uni.request + 统一 Result 解包 + token 注入）。
- 后端地址常量：`BASE_URL = http://localhost:8090`，真机调试时改为局域网 IP。
- 微信开发者工具需开启「不校验合法域名」。
