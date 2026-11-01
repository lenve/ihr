package com.ihr.module.salary.controller;

import com.ihr.common.result.Result;
import com.ihr.module.salary.service.SalaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 薪酬模块骨架（工资查询、薪酬核算等）。
 */
@Tag(name = "薪酬模块")
@RestController
@RequestMapping("/api/salary")
public class SalaryController {

    private final SalaryService salaryService;

    public SalaryController(SalaryService salaryService) {
        this.salaryService = salaryService;
    }

    @Operation(summary = "模块探活", description = "框架占位，后续在此补充各业务接口")
    @GetMapping("/ping")
    public Result<String> ping() {
        return Result.ok(salaryService.moduleStatus());
    }
}
