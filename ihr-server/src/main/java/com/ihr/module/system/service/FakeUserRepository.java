package com.ihr.module.system.service;

import com.ihr.module.system.vo.UserInfoVO;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 框架阶段假数据用户仓库，与 MySQL 初始化脚本中的数据保持一致：
 * <pre>
 * admin / 123456    -> ADMIN（系统管理员）
 * zhangsan / 123456 -> EMPLOYEE（普通员工）
 * lisi / 123456     -> HR（人事专员）
 * </pre>
 */
public class FakeUserRepository {

    private static final Map<String, UserInfoVO> USERS = Map.of(
            "admin", new UserInfoVO(1L, "admin", "系统管理员", "admin@ihr.com", "13800000000", List.of("ADMIN")),
            "zhangsan", new UserInfoVO(2L, "zhangsan", "张三", "zhangsan@ihr.com", "13800000001", List.of("EMPLOYEE")),
            "lisi", new UserInfoVO(3L, "lisi", "李四", "lisi@ihr.com", "13800000002", List.of("HR"))
    );

    private static final String DEMO_PASSWORD = "123456";

    public Optional<UserInfoVO> findByUsername(String username) {
        return Optional.ofNullable(USERS.get(username));
    }

    public boolean checkPassword(String username, String password) {
        return DEMO_PASSWORD.equals(password) && USERS.containsKey(username);
    }
}
