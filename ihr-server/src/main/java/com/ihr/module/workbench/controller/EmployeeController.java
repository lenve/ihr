package com.ihr.module.workbench.controller;

import com.ihr.common.exception.BusinessException;
import com.ihr.common.result.Result;
import com.ihr.module.workbench.entity.Employee;
import com.ihr.module.workbench.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 个人信息接口（当前登录用户档案）。
 */
@Tag(name = "11-个人工作台-个人信息")
@RestController
@RequestMapping("/api/workbench/profile")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Operation(summary = "查询当前用户档案")
    @GetMapping
    public Result<Employee> profile() {
        String realName = employeeService.currentRealName();
        Employee employee = employeeService.lambdaQuery()
                .eq(Employee::getRealName, realName)
                .last("LIMIT 1")
                .one();
        if (employee == null) {
            throw new BusinessException("未找到当前用户档案");
        }
        return Result.ok(employee);
    }

    @Operation(summary = "修改个人信息", description = "仅允许修改联系电话、邮箱、工作地、备注字段")
    @PutMapping
    public Result<Void> update(@RequestBody Employee body) {
        if (body == null || body.getId() == null) {
            throw new BusinessException("缺少档案 ID");
        }
        Employee patch = new Employee();
        patch.setId(body.getId());
        patch.setPhone(body.getPhone());
        patch.setEmail(body.getEmail());
        patch.setWorkPlace(body.getWorkPlace());
        patch.setRemark(body.getRemark());
        employeeService.updateById(patch);
        return Result.ok();
    }
}
