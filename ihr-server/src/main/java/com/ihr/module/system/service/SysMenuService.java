package com.ihr.module.system.service;

import com.ihr.module.system.vo.MenuTreeVO;

import java.util.List;

/**
 * 菜单服务骨架。
 */
public interface SysMenuService {

    /** 查询全量菜单树（框架阶段：网页端菜单由前端路由配置提供，此接口供动态菜单演进使用） */
    List<MenuTreeVO> listMenuTree();
}
