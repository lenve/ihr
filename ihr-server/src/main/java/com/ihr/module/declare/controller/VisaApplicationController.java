package com.ihr.module.declare.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ihr.common.result.Result;
import com.ihr.module.declare.entity.VisaApplication;
import com.ihr.module.declare.service.VisaApplicationService;
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
 * 签证申请接口。
 */
@Tag(name = "50-申报与审批-签证申请")
@RestController
@RequestMapping("/api/declare/visas")
public class VisaApplicationController {

    private final VisaApplicationService visaApplicationService;

    public VisaApplicationController(VisaApplicationService visaApplicationService) {
        this.visaApplicationService = visaApplicationService;
    }

    @Operation(summary = "分页查询")
    @GetMapping
    public Result<IPage<VisaApplication>> page(@RequestParam(defaultValue = "1") long pageNum,
                                               @RequestParam(defaultValue = "10") long pageSize,
                                               @RequestParam(required = false) String keyword,
                                               @RequestParam(required = false) Integer status) {
        return Result.ok(visaApplicationService.page(pageNum, pageSize, keyword, status));
    }

    @Operation(summary = "新增")
    @PostMapping
    public Result<Void> create(@RequestBody VisaApplication visaApplication) {
        visaApplication.setId(null);
        visaApplicationService.save(visaApplication);
        return Result.ok();
    }

    @Operation(summary = "修改")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody VisaApplication visaApplication) {
        visaApplication.setId(id);
        visaApplicationService.updateById(visaApplication);
        return Result.ok();
    }

    @Operation(summary = "删除")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        visaApplicationService.removeById(id);
        return Result.ok();
    }

    @Operation(summary = "通过")
    @PutMapping("/{id}/approve")
    public Result<Void> approve(@PathVariable Long id) {
        visaApplicationService.approve(id);
        return Result.ok();
    }

    @Operation(summary = "驳回")
    @PutMapping("/{id}/reject")
    public Result<Void> reject(@PathVariable Long id) {
        visaApplicationService.reject(id);
        return Result.ok();
    }

    @Operation(summary = "已办理")
    @PutMapping("/{id}/done")
    public Result<Void> done(@PathVariable Long id) {
        visaApplicationService.done(id);
        return Result.ok();
    }
}
