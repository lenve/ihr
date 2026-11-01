package com.ihr.module.performance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ihr.common.result.Result;
import com.ihr.module.performance.dto.InterviewFillDTO;
import com.ihr.module.performance.entity.PerfInterview;
import com.ihr.module.performance.service.PerfInterviewService;
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
 * 绩效面谈接口。
 */
@Tag(name = "绩效考核-绩效面谈")
@RestController
@RequestMapping("/api/performance/interviews")
public class PerfInterviewController {

    private final PerfInterviewService perfInterviewService;

    public PerfInterviewController(PerfInterviewService perfInterviewService) {
        this.perfInterviewService = perfInterviewService;
    }

    @Operation(summary = "分页查询")
    @GetMapping
    public Result<IPage<PerfInterview>> page(@RequestParam(defaultValue = "1") long pageNum,
                                             @RequestParam(defaultValue = "10") long pageSize,
                                             @RequestParam(required = false) String keyword,
                                             @RequestParam(required = false) Integer fillStatus,
                                             @RequestParam(required = false) Integer auditStatus) {
        return Result.ok(perfInterviewService.page(pageNum, pageSize, keyword, fillStatus, auditStatus));
    }

    @Operation(summary = "新增")
    @PostMapping
    public Result<Void> create(@RequestBody PerfInterview perfInterview) {
        perfInterview.setId(null);
        perfInterviewService.save(perfInterview);
        return Result.ok();
    }

    @Operation(summary = "修改")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody PerfInterview perfInterview) {
        perfInterview.setId(id);
        perfInterviewService.updateById(perfInterview);
        return Result.ok();
    }

    @Operation(summary = "删除")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        perfInterviewService.removeById(id);
        return Result.ok();
    }

    @Operation(summary = "填写面谈记录", description = "fillStatus=1，并把考核由待面谈推进到面谈审核")
    @PutMapping("/{id}/fill")
    public Result<Void> fill(@PathVariable Long id, @RequestBody InterviewFillDTO dto) {
        perfInterviewService.fill(id, dto);
        return Result.ok();
    }

    @Operation(summary = "审核面谈记录", description = "passed=true 通过（考核→待签署）；false 驳回（考核回待面谈）")
    @PutMapping("/{id}/audit")
    public Result<Void> audit(@PathVariable Long id, @RequestParam boolean passed) {
        perfInterviewService.audit(id, passed);
        return Result.ok();
    }
}
