package com.ihr.module.performance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ihr.common.result.Result;
import com.ihr.module.performance.entity.PerfAchievement;
import com.ihr.module.performance.service.PerfAchievementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 业绩记录与团队业绩申请接口。
 */
@Tag(name = "绩效考核-业绩记录")
@RestController
@RequestMapping("/api/performance/achievements")
public class PerfAchievementController {

    private final PerfAchievementService perfAchievementService;

    public PerfAchievementController(PerfAchievementService perfAchievementService) {
        this.perfAchievementService = perfAchievementService;
    }

    @Operation(summary = "分页查询")
    @GetMapping
    public Result<IPage<PerfAchievement>> page(@RequestParam(defaultValue = "1") long pageNum,
                                               @RequestParam(defaultValue = "10") long pageSize,
                                               @RequestParam(required = false) String keyword,
                                               @RequestParam(required = false) Integer achieveType,
                                               @RequestParam(required = false) Integer status) {
        return Result.ok(perfAchievementService.page(pageNum, pageSize, keyword, achieveType, status));
    }

    @Operation(summary = "新增")
    @PostMapping
    public Result<Void> create(@RequestBody PerfAchievement perfAchievement) {
        perfAchievement.setId(null);
        perfAchievementService.save(perfAchievement);
        return Result.ok();
    }

    @Operation(summary = "修改")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody PerfAchievement perfAchievement) {
        perfAchievement.setId(id);
        perfAchievementService.updateById(perfAchievement);
        return Result.ok();
    }

    @Operation(summary = "删除")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        perfAchievementService.removeById(id);
        return Result.ok();
    }

    @Operation(summary = "提交：status 0→1")
    @PutMapping("/{id}/submit")
    public Result<Void> submit(@PathVariable Long id) {
        perfAchievementService.submit(id);
        return Result.ok();
    }

    @Operation(summary = "确认：status 1→2")
    @PutMapping("/{id}/confirm")
    public Result<Void> confirm(@PathVariable Long id,
                                @RequestParam(required = false) String auditComment) {
        perfAchievementService.confirm(id, auditComment);
        return Result.ok();
    }

    @Operation(summary = "驳回：status 1→3")
    @PutMapping("/{id}/reject")
    public Result<Void> reject(@PathVariable Long id,
                               @RequestParam(required = false) String auditComment) {
        perfAchievementService.reject(id, auditComment);
        return Result.ok();
    }
}
