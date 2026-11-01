package com.ihr.module.hrservice.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ihr.common.result.Result;
import com.ihr.module.hrservice.entity.VehicleViolation;
import com.ihr.module.hrservice.service.VehicleViolationService;
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
 * 车辆违规接口。
 */
@Tag(name = "40-人事服务-车辆违规")
@RestController
@RequestMapping("/api/hrservice/vehicle-violations")
public class VehicleViolationController {

    private final VehicleViolationService vehicleViolationService;

    public VehicleViolationController(VehicleViolationService vehicleViolationService) {
        this.vehicleViolationService = vehicleViolationService;
    }

    @Operation(summary = "分页查询")
    @GetMapping
    public Result<IPage<VehicleViolation>> page(@RequestParam(defaultValue = "1") long pageNum,
                                                @RequestParam(defaultValue = "10") long pageSize,
                                                @RequestParam(required = false) String keyword,
                                                @RequestParam(required = false) Integer deductStatus) {
        return Result.ok(vehicleViolationService.page(pageNum, pageSize, keyword, deductStatus));
    }

    @Operation(summary = "新增")
    @PostMapping
    public Result<Void> create(@RequestBody VehicleViolation vehicleViolation) {
        vehicleViolation.setId(null);
        vehicleViolationService.save(vehicleViolation);
        return Result.ok();
    }

    @Operation(summary = "修改")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody VehicleViolation vehicleViolation) {
        vehicleViolation.setId(id);
        vehicleViolationService.updateById(vehicleViolation);
        return Result.ok();
    }

    @Operation(summary = "删除")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        vehicleViolationService.removeById(id);
        return Result.ok();
    }

    @Operation(summary = "处理")
    @PutMapping("/{id}/handle")
    public Result<Void> handle(@PathVariable Long id) {
        vehicleViolationService.handle(id);
        return Result.ok();
    }
}
