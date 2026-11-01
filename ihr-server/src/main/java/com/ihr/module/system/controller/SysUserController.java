package com.ihr.module.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ihr.common.result.Result;
import com.ihr.module.system.entity.SysUser;
import com.ihr.module.system.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统用户接口骨架（查询 sys_user 表，需登录 + MySQL 运行）。
 */
@Tag(name = "02-系统管理-用户")
@RestController
@RequestMapping("/api/system/users")
public class SysUserController {

    private final SysUserService sysUserService;

    public SysUserController(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @Operation(summary = "分页查询用户", description = "演示 MyBatis-Plus 分页骨架")
    @GetMapping
    public Result<IPage<SysUser>> page(@RequestParam(defaultValue = "1") long pageNum,
                                       @RequestParam(defaultValue = "10") long pageSize) {
        return Result.ok(sysUserService.pageUsers(pageNum, pageSize));
    }
}
