package com.ihr.module.performance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ihr.common.result.Result;
import com.ihr.module.performance.dto.AppealAuditDTO;
import com.ihr.module.performance.entity.PerfAppeal;
import com.ihr.module.performance.service.PerfAppealService;
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
 * 绩效申诉接口。
 */
@Tag(name = "绩效考核-绩效申诉")
@RestController
@RequestMapping("/api/performance/appeals")
public class PerfAppealController {

    private final PerfAppealService perfAppealService;

    public PerfAppealController(PerfAppealService perfAppealService) {
        this.perfAppealService = perfAppealService;
    }

    @Operation(summary = "分页查询")
    @GetMapping
    public Result<IPage<PerfAppeal>> page(@RequestParam(defaultValue = "1") long pageNum,
                                          @RequestParam(defaultValue = "10") long pageSize,
                                          @RequestParam(required = false) String keyword,
                                          @RequestParam(required = false) Integer status,
                                          @RequestParam(required = false) Long appraisalId) {
        return Result.ok(perfAppealService.page(pageNum, pageSize, keyword, status, appraisalId));
    }

    @Operation(summary = "发起申诉", description = "未填 emp_name 时取当前登录人；创建后对应考核进入申诉中")
    @PostMapping
    public Result<Void> create(@RequestBody PerfAppeal perfAppeal) {
        perfAppealService.createAppeal(perfAppeal);
        return Result.ok();
    }

    @Operation(summary = "修改")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody PerfAppeal perfAppeal) {
        perfAppeal.setId(id);
        perfAppealService.updateById(perfAppeal);
        return Result.ok();
    }

    @Operation(summary = "删除")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        perfAppealService.removeById(id);
        return Result.ok();
    }

    @Operation(summary = "审核申诉", description = "status：1已受理/2已驳回/3已调整；驳回或调整后考核回已完成")
    @PutMapping("/{id}/audit")
    public Result<Void> audit(@PathVariable Long id, @RequestBody AppealAuditDTO dto) {
        perfAppealService.audit(id, dto);
        return Result.ok();
    }
}
