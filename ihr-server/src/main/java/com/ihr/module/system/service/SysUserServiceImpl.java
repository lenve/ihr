package com.ihr.module.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ihr.module.system.entity.SysUser;
import com.ihr.module.system.mapper.SysUserMapper;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现骨架。
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Override
    public IPage<SysUser> pageUsers(long pageNum, long pageSize) {
        // 框架占位：仅演示分页骨架，真实业务需过滤敏感字段、联查角色
        return this.page(new Page<>(pageNum, pageSize));
    }
}
