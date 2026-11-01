package com.ihr.module.recruit.controller;

import com.ihr.common.result.Result;
import com.ihr.module.recruit.service.RecruitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 招聘与人才模块骨架。
 * <p>对应网页端菜单：招聘信息、人才管理、人才申报、人力资源池。</p>
 */
@Tag(name = "招聘与人才")
@RestController
@RequestMapping("/api/recruit")
public class RecruitController {

    private final RecruitService recruitService;

    public RecruitController(RecruitService recruitService) {
        this.recruitService = recruitService;
    }

    @Operation(summary = "模块探活", description = "框架占位，后续在此补充各业务接口")
    @GetMapping("/ping")
    public Result<String> ping() {
        return Result.ok(recruitService.moduleStatus());
    }
}
