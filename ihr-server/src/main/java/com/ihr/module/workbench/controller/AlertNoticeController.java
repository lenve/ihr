package com.ihr.module.workbench.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ihr.common.result.Result;
import com.ihr.module.workbench.entity.AlertNotice;
import com.ihr.module.workbench.service.AlertNoticeService;
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
 * 预警通知接口。
 */
@Tag(name = "13-个人工作台-预警通知")
@RestController
@RequestMapping("/api/workbench/alerts")
public class AlertNoticeController {

    private final AlertNoticeService alertNoticeService;

    public AlertNoticeController(AlertNoticeService alertNoticeService) {
        this.alertNoticeService = alertNoticeService;
    }

    @Operation(summary = "分页查询")
    @GetMapping
    public Result<IPage<AlertNotice>> page(@RequestParam(defaultValue = "1") long pageNum,
                                           @RequestParam(defaultValue = "10") long pageSize,
                                           @RequestParam(required = false) String keyword,
                                           @RequestParam(required = false) Integer status,
                                           @RequestParam(required = false) Integer level) {
        return Result.ok(alertNoticeService.page(pageNum, pageSize, keyword, status, level));
    }

    @Operation(summary = "新增")
    @PostMapping
    public Result<Void> create(@RequestBody AlertNotice alertNotice) {
        alertNotice.setId(null);
        alertNoticeService.save(alertNotice);
        return Result.ok();
    }

    @Operation(summary = "修改")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody AlertNotice alertNotice) {
        alertNotice.setId(id);
        alertNoticeService.updateById(alertNotice);
        return Result.ok();
    }

    @Operation(summary = "删除")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        alertNoticeService.removeById(id);
        return Result.ok();
    }

    @Operation(summary = "标记已处理")
    @PutMapping("/{id}/handle")
    public Result<Void> handle(@PathVariable Long id) {
        alertNoticeService.handle(id);
        return Result.ok();
    }
}
