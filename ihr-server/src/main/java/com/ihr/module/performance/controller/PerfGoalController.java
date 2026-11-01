package com.ihr.module.performance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ihr.common.result.Result;
import com.ihr.module.performance.entity.PerfGoal;
import com.ihr.module.performance.service.PerfGoalService;
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

import java.util.List;

/**
 * 绩效目标接口。
 */
@Tag(name = "绩效考核-考核目标")
@RestController
@RequestMapping("/api/performance/goals")
public class PerfGoalController {

    private final PerfGoalService perfGoalService;

    public PerfGoalController(PerfGoalService perfGoalService) {
        this.perfGoalService = perfGoalService;
    }

    @Operation(summary = "查询目标", description = "传 appraisalId 返回该考核下全部目标列表，否则按 keyword 分页")
    @GetMapping
    public Result<Object> list(@RequestParam(required = false) Long appraisalId,
                               @RequestParam(required = false) String keyword,
                               @RequestParam(defaultValue = "1") long pageNum,
                               @RequestParam(defaultValue = "10") long pageSize) {
        if (appraisalId != null) {
            return Result.ok(perfGoalService.listByAppraisal(appraisalId));
        }
        IPage<PerfGoal> page = perfGoalService.page(pageNum, pageSize, keyword, null);
        return Result.ok(page);
    }

    @Operation(summary = "新增目标")
    @PostMapping
    public Result<Void> create(@RequestBody PerfGoal perfGoal) {
        perfGoal.setId(null);
        perfGoalService.save(perfGoal);
        return Result.ok();
    }

    @Operation(summary = "修改目标", description = "填写自评分时若考核处于目标确认状态则自动进入自评中")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody PerfGoal perfGoal) {
        perfGoal.setId(id);
        perfGoalService.updateGoal(perfGoal);
        return Result.ok();
    }

    @Operation(summary = "删除目标")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        perfGoalService.removeById(id);
        return Result.ok();
    }
}
