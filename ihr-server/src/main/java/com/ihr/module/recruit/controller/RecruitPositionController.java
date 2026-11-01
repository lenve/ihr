package com.ihr.module.recruit.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ihr.common.result.Result;
import com.ihr.module.recruit.entity.RecruitPosition;
import com.ihr.module.recruit.service.RecruitPositionService;
import com.ihr.module.workbench.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * 招聘职位接口。
 */
@Tag(name = "20-招聘与人才-招聘职位")
@RestController
@RequestMapping("/api/recruit/positions")
public class RecruitPositionController {

    private final RecruitPositionService recruitPositionService;
    private final EmployeeService employeeService;

    public RecruitPositionController(RecruitPositionService recruitPositionService,
                                     EmployeeService employeeService) {
        this.recruitPositionService = recruitPositionService;
        this.employeeService = employeeService;
    }

    @Operation(summary = "分页查询")
    @GetMapping
    public Result<IPage<RecruitPosition>> page(@RequestParam(defaultValue = "1") long pageNum,
                                               @RequestParam(defaultValue = "10") long pageSize,
                                               @RequestParam(required = false) String keyword,
                                               @RequestParam(required = false) Integer status) {
        return Result.ok(recruitPositionService.page(pageNum, pageSize, keyword, status));
    }

    @Operation(summary = "新增", description = "发布人与发布时间为空时自动填充当前用户与当前时间")
    @PostMapping
    public Result<Void> create(@RequestBody RecruitPosition position) {
        position.setId(null);
        if (!StringUtils.hasText(position.getPublisher())) {
            position.setPublisher(employeeService.currentRealName());
        }
        if (position.getPublishTime() == null) {
            position.setPublishTime(LocalDateTime.now());
        }
        recruitPositionService.save(position);
        return Result.ok();
    }

    @Operation(summary = "修改")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody RecruitPosition position) {
        position.setId(id);
        recruitPositionService.updateById(position);
        return Result.ok();
    }

    @Operation(summary = "删除")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        recruitPositionService.removeById(id);
        return Result.ok();
    }
}
