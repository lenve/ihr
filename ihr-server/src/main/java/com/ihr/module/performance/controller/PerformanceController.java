package com.ihr.module.performance.controller;

import com.ihr.common.result.Result;
import com.ihr.module.performance.service.PerformanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 绩效考核模块骨架。
 * <p>对应网页端菜单：考核表评分、考核关系、我的业绩、团队业绩申请、
 * 我的绩效（目标制定/自评/上级评价/签署进度/面谈/结果/申诉等）。</p>
 */
@Tag(name = "绩效考核")
@RestController
@RequestMapping("/api/performance")
public class PerformanceController {

    private final PerformanceService performanceService;

    public PerformanceController(PerformanceService performanceService) {
        this.performanceService = performanceService;
    }

    @Operation(summary = "模块探活", description = "框架占位，后续在此补充各业务接口")
    @GetMapping("/ping")
    public Result<String> ping() {
        return Result.ok(performanceService.moduleStatus());
    }
}
