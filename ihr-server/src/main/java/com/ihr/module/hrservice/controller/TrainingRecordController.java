package com.ihr.module.hrservice.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ihr.common.result.Result;
import com.ihr.module.hrservice.entity.TrainingRecord;
import com.ihr.module.hrservice.service.TrainingRecordService;
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
 * 培训档案接口。
 */
@Tag(name = "40-人事服务-培训档案")
@RestController
@RequestMapping("/api/hrservice/trainings")
public class TrainingRecordController {

    private final TrainingRecordService trainingRecordService;

    public TrainingRecordController(TrainingRecordService trainingRecordService) {
        this.trainingRecordService = trainingRecordService;
    }

    @Operation(summary = "分页查询")
    @GetMapping
    public Result<IPage<TrainingRecord>> page(@RequestParam(defaultValue = "1") long pageNum,
                                              @RequestParam(defaultValue = "10") long pageSize,
                                              @RequestParam(required = false) String keyword) {
        return Result.ok(trainingRecordService.page(pageNum, pageSize, keyword));
    }

    @Operation(summary = "新增")
    @PostMapping
    public Result<Void> create(@RequestBody TrainingRecord trainingRecord) {
        trainingRecord.setId(null);
        trainingRecordService.save(trainingRecord);
        return Result.ok();
    }

    @Operation(summary = "修改")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody TrainingRecord trainingRecord) {
        trainingRecord.setId(id);
        trainingRecordService.updateById(trainingRecord);
        return Result.ok();
    }

    @Operation(summary = "删除")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        trainingRecordService.removeById(id);
        return Result.ok();
    }
}
