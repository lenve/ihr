-- ============================================================
-- iHR 智慧人力资源管理系统 - MySQL 初始化脚本
-- 由 docker-entrypoint-initdb.d 在容器首次启动时自动执行
-- ============================================================

CREATE DATABASE IF NOT EXISTS `ihr`
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;

USE `ihr`;

-- ------------------------------------------------------------
-- 用户表
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username`    VARCHAR(64)  NOT NULL COMMENT '登录账号',
  `password`    VARCHAR(128) NOT NULL COMMENT '密码（接入真实登录后存 BCrypt 哈希，当前为框架演示数据）',
  `nickname`    VARCHAR(64)  NOT NULL DEFAULT '' COMMENT '昵称',
  `email`       VARCHAR(128) DEFAULT NULL COMMENT '邮箱',
  `phone`       VARCHAR(32)  DEFAULT NULL COMMENT '手机号',
  `status`      TINYINT      NOT NULL DEFAULT 1 COMMENT '状态：1-启用 0-禁用',
  `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE = InnoDB COMMENT = '系统用户表';

-- ------------------------------------------------------------
-- 角色表
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id`          BIGINT      NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_code`   VARCHAR(64) NOT NULL COMMENT '角色编码',
  `role_name`   VARCHAR(64) NOT NULL COMMENT '角色名称',
  `status`      TINYINT     NOT NULL DEFAULT 1 COMMENT '状态：1-启用 0-禁用',
  `remark`      VARCHAR(255) DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_code` (`role_code`)
) ENGINE = InnoDB COMMENT = '系统角色表';

-- ------------------------------------------------------------
-- 菜单表（与网页端侧边栏分组一致）
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `parent_id`   BIGINT       NOT NULL DEFAULT 0 COMMENT '父菜单ID，0为顶级',
  `menu_name`   VARCHAR(64)  NOT NULL COMMENT '菜单名称',
  `menu_type`   TINYINT      NOT NULL DEFAULT 1 COMMENT '类型：1-目录 2-菜单 3-按钮',
  `path`        VARCHAR(255) DEFAULT NULL COMMENT '前端路由路径',
  `icon`        VARCHAR(64)  DEFAULT NULL COMMENT '图标',
  `sort_no`     INT          NOT NULL DEFAULT 0 COMMENT '排序号',
  `status`      TINYINT      NOT NULL DEFAULT 1 COMMENT '状态：1-启用 0-禁用',
  `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE = InnoDB COMMENT = '系统菜单表';

-- ------------------------------------------------------------
-- 用户-角色关联表
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `role_id` BIGINT NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`)
) ENGINE = InnoDB COMMENT = '用户角色关联表';

-- ------------------------------------------------------------
-- 角色-菜单关联表
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `role_id` BIGINT NOT NULL COMMENT '角色ID',
  `menu_id` BIGINT NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`, `menu_id`)
) ENGINE = InnoDB COMMENT = '角色菜单关联表';

-- ============================================================
-- 初始化数据
-- 说明：登录接口当前为 JWT 假数据校验（admin/123456），
--       接入真实业务时改为查询 sys_user 并校验 BCrypt 密码。
-- ============================================================
INSERT INTO `sys_user` (`id`, `username`, `password`, `nickname`, `email`, `phone`, `status`) VALUES
  (1, 'admin', '123456', '系统管理员', 'admin@ihr.com', '13800000000', 1),
  (2, 'zhangsan', '123456', '张三', 'zhangsan@ihr.com', '13800000001', 1),
  (3, 'lisi', '123456', '李四', 'lisi@ihr.com', '13800000002', 1);

INSERT INTO `sys_role` (`id`, `role_code`, `role_name`, `status`, `remark`) VALUES
  (1, 'ADMIN', '系统管理员', 1, '拥有全部菜单权限'),
  (2, 'EMPLOYEE', '普通员工', 1, '基础权限'),
  (3, 'HR', '人事专员', 1, '人事服务相关权限');

-- 顶级菜单与网页端侧边栏分组一致（子菜单由前端路由配置，后续可全量入表做动态菜单）
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `icon`, `sort_no`) VALUES
  (100, 0, '个人工作台', 1, '/workspace',        'Monitor',       1),
  (200, 0, '招聘与人才', 1, '/recruit',          'User',          2),
  (300, 0, '绩效考核',   1, '/performance',      'DataAnalysis',  3),
  (400, 0, '人事服务',   1, '/hr-service',       'Service',       4),
  (500, 0, '申报与审批', 1, '/declare',          'Document',      5),
  (600, 0, '平台功能',   1, '/platform',         'Message',       6);

-- 示例子菜单（个人工作台）
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `icon`, `sort_no`) VALUES
  (101, 100, '个人信息', 2, '/workspace/personal-info', NULL, 1),
  (102, 100, '待办通知', 2, '/workspace/todo-notice',   NULL, 2),
  (103, 100, '工作任务', 2, '/workspace/work-task',     NULL, 3),
  (104, 100, '预警通知', 2, '/workspace/alert-notice',  NULL, 4);

-- 用户-角色关联
INSERT INTO `sys_user_role` (`user_id`, `role_id`) VALUES
  (1, 1), (2, 2), (3, 3);

-- 角色-菜单关联：ADMIN 拥有全部菜单
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES
  (1, 100), (1, 101), (1, 102), (1, 103), (1, 104),
  (1, 200), (1, 300), (1, 400), (1, 500), (1, 600),
  (2, 100), (2, 400),
  (3, 100), (3, 200), (3, 400);
