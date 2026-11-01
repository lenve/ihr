package com.ihr.module.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ihr.module.system.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户表 Mapper。
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
}
