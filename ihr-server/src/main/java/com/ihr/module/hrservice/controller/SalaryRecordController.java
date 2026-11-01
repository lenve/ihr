package com.ihr.module.hrservice.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ihr.common.result.Result;
import com.ihr.module.hrservice.entity.SalaryRecord;
import com.ihr.module.hrservice.service.SalaryRecordService;
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
 * 工资记录接口。
 */
@Tag(name = "40-人事服务-工资记录")
@RestController
@RequestMapping("/api/hrservice/salaries")
public class SalaryRecordController {

    private final SalaryRecordService salaryRecordService;

    public SalaryRecordController(SalaryRecordService salaryRecordService) {
        this.salaryRecordService = salaryRecordService;
    }

    @Operation(summary = "分页查询")
    @GetMapping
    public Result<IPage<SalaryRecord>> page(@RequestParam(defaultValue = "1") long pageNum,
                                            @RequestParam(defaultValue = "10") long pageSize,
                                            @RequestParam(required = false) String keyword,
                                            @RequestParam(required = false) String salaryMonth,
                                            @RequestParam(required = false) Integer status) {
        return Result.ok(salaryRecordService.page(pageNum, pageSize, keyword, salaryMonth, status));
    }

    @Operation(summary = "新增")
    @PostMapping
    public Result<Void> create(@RequestBody SalaryRecord salaryRecord) {
        salaryRecord.setId(null);
        salaryRecordService.save(salaryRecord);
        return Result.ok();
    }

    @Operation(summary = "修改")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody SalaryRecord salaryRecord) {
        salaryRecord.setId(id);
        salaryRecordService.updateById(salaryRecord);
        return Result.ok();
    }

    @Operation(summary = "删除")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        salaryRecordService.removeById(id);
        return Result.ok();
    }
}
