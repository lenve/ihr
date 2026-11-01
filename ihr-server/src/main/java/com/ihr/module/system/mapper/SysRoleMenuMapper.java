package com.ihr.module.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ihr.module.system.entity.SysRoleMenu;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色-菜单关联表 Mapper。
 */
@Mapper
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {
}
