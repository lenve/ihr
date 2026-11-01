package com.ihr.module.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ihr.module.system.entity.SysUser;

/**
 * 用户服务骨架。
 */
public interface SysUserService extends IService<SysUser> {

    /** 分页查询用户（框架占位：后续补充角色/部门等联查） */
    IPage<SysUser> pageUsers(long pageNum, long pageSize);
}
