package com.ihr.module.workbench.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ihr.common.result.Result;
import com.ihr.module.workbench.entity.WorkTask;
import com.ihr.module.workbench.service.WorkTaskService;
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
 * 工作任务接口。
 */
@Tag(name = "12-个人工作台-工作任务")
@RestController
@RequestMapping("/api/workbench/tasks")
public class WorkTaskController {

    private final WorkTaskService workTaskService;

    public WorkTaskController(WorkTaskService workTaskService) {
        this.workTaskService = workTaskService;
    }

    @Operation(summary = "分页查询")
    @GetMapping
    public Result<IPage<WorkTask>> page(@RequestParam(defaultValue = "1") long pageNum,
                                        @RequestParam(defaultValue = "10") long pageSize,
                                        @RequestParam(required = false) String keyword,
                                        @RequestParam(required = false) Integer status) {
        return Result.ok(workTaskService.page(pageNum, pageSize, keyword, status));
    }

    @Operation(summary = "新增")
    @PostMapping
    public Result<Void> create(@RequestBody WorkTask workTask) {
        workTask.setId(null);
        workTaskService.save(workTask);
        return Result.ok();
    }

    @Operation(summary = "修改")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody WorkTask workTask) {
        workTask.setId(id);
        workTaskService.updateById(workTask);
        return Result.ok();
    }

    @Operation(summary = "删除")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        workTaskService.removeById(id);
        return Result.ok();
    }

    @Operation(summary = "标记完成", description = "status=1、progress=100、finishTime=当前时间")
    @PutMapping("/{id}/complete")
    public Result<Void> complete(@PathVariable Long id) {
        workTaskService.complete(id);
        return Result.ok();
    }
}
