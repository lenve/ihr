package com.ihr.module.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ihr.module.system.entity.SysUserRole;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户-角色关联表 Mapper。
 */
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {
}
