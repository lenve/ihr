package com.ihr.module.performance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ihr.common.result.Result;
import com.ihr.module.performance.entity.PerfAppraisal;
import com.ihr.module.performance.service.PerfAppraisalService;
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

import java.util.Map;

/**
 * 绩效考核主表接口（含流程状态机动作）。
 */
@Tag(name = "绩效考核-考核单")
@RestController
@RequestMapping("/api/performance/appraisals")
public class PerfAppraisalController {

    private final PerfAppraisalService perfAppraisalService;

    public PerfAppraisalController(PerfAppraisalService perfAppraisalService) {
        this.perfAppraisalService = perfAppraisalService;
    }

    @Operation(summary = "分页查询")
    @GetMapping
    public Result<IPage<PerfAppraisal>> page(@RequestParam(defaultValue = "1") long pageNum,
                                             @RequestParam(defaultValue = "10") long pageSize,
                                             @RequestParam(required = false) String keyword,
                                             @RequestParam(required = false) String period,
                                             @RequestParam(required = false) Integer status) {
        return Result.ok(perfAppraisalService.page(pageNum, pageSize, keyword, period, status));
    }

    @Operation(summary = "新增")
    @PostMapping
    public Result<Void> create(@RequestBody PerfAppraisal perfAppraisal) {
        perfAppraisal.setId(null);
        perfAppraisalService.save(perfAppraisal);
        return Result.ok();
    }

    @Operation(summary = "修改")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody PerfAppraisal perfAppraisal) {
        perfAppraisal.setId(id);
        perfAppraisalService.updateById(perfAppraisal);
        return Result.ok();
    }

    @Operation(summary = "删除")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        perfAppraisalService.removeById(id);
        return Result.ok();
    }

    @Operation(summary = "确认目标：status 0→1")
    @PutMapping("/{id}/confirm-goal")
    public Result<Void> confirmGoal(@PathVariable Long id) {
        perfAppraisalService.confirmGoal(id);
        return Result.ok();
    }

    @Operation(summary = "提交自评：校验全部目标已自评并计算自评总分，status→3")
    @PutMapping("/{id}/submit-self-review")
    public Result<Void> submitSelfReview(@PathVariable Long id) {
        perfAppraisalService.submitSelfReview(id);
        return Result.ok();
    }

    @Operation(summary = "提交上级评价：计算上级总分/综合分/等级，status→4")
    @PutMapping("/{id}/submit-superior-review")
    public Result<Void> submitSuperiorReview(@PathVariable Long id) {
        perfAppraisalService.submitSuperiorReview(id);
        return Result.ok();
    }

    @Operation(summary = "面谈审核通过：status 5→6")
    @PutMapping("/{id}/interview-passed")
    public Result<Void> interviewPassed(@PathVariable Long id) {
        perfAppraisalService.interviewPassed(id);
        return Result.ok();
    }

    @Operation(summary = "签署：signStatus=1，status→7")
    @PutMapping("/{id}/sign")
    public Result<Void> sign(@PathVariable Long id) {
        perfAppraisalService.sign(id);
        return Result.ok();
    }

    @Operation(summary = "考核详情：考核 + 目标列表 + 面谈记录")
    @GetMapping("/{id}/detail")
    public Result<Map<String, Object>> detail(@PathVariable Long id) {
        return Result.ok(perfAppraisalService.detail(id));
    }
}
