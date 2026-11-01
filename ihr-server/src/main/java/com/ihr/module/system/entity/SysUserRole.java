package com.ihr.module.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 用户-角色关联表实体。
 */
@TableName("sys_user_role")
public class SysUserRole {

    private Long userId;

    private Long roleId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
