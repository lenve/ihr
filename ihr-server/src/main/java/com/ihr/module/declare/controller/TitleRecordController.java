package com.ihr.module.declare.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ihr.common.result.Result;
import com.ihr.module.declare.entity.TitleRecord;
import com.ihr.module.declare.service.TitleRecordService;
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
 * 职称记录接口（我的职称）。
 */
@Tag(name = "50-申报与审批-职称记录")
@RestController
@RequestMapping("/api/declare/title-records")
public class TitleRecordController {

    private final TitleRecordService titleRecordService;

    public TitleRecordController(TitleRecordService titleRecordService) {
        this.titleRecordService = titleRecordService;
    }

    @Operation(summary = "分页查询")
    @GetMapping
    public Result<IPage<TitleRecord>> page(@RequestParam(defaultValue = "1") long pageNum,
                                           @RequestParam(defaultValue = "10") long pageSize,
                                           @RequestParam(required = false) String keyword,
                                           @RequestParam(required = false) Integer status) {
        return Result.ok(titleRecordService.page(pageNum, pageSize, keyword, status));
    }

    @Operation(summary = "新增")
    @PostMapping
    public Result<Void> create(@RequestBody TitleRecord titleRecord) {
        titleRecord.setId(null);
        titleRecordService.save(titleRecord);
        return Result.ok();
    }

    @Operation(summary = "修改")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody TitleRecord titleRecord) {
        titleRecord.setId(id);
        titleRecordService.updateById(titleRecord);
        return Result.ok();
    }

    @Operation(summary = "删除")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        titleRecordService.removeById(id);
        return Result.ok();
    }
}
