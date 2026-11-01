package com.ihr.module.system.controller;

import com.ihr.common.result.Result;
import com.ihr.module.system.service.SysMenuService;
import com.ihr.module.system.vo.MenuTreeVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 菜单接口骨架：返回 sys_menu 表构建的菜单树（需登录 + MySQL 运行）。
 */
@Tag(name = "02-系统管理-菜单")
@RestController
@RequestMapping("/api/system/menus")
public class SysMenuController {

    private final SysMenuService sysMenuService;

    public SysMenuController(SysMenuService sysMenuService) {
        this.sysMenuService = sysMenuService;
    }

    @Operation(summary = "查询菜单树", description = "框架阶段网页端菜单由前端路由配置，此接口供动态菜单演进")
    @GetMapping
    public Result<List<MenuTreeVO>> tree() {
        return Result.ok(sysMenuService.listMenuTree());
    }
}
