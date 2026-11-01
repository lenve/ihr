package com.ihr.module.hrservice.controller;

import com.ihr.common.result.Result;
import com.ihr.module.workbench.entity.Employee;
import com.ihr.module.workbench.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 人事报表接口：从员工档案（emp_employee）聚合统计。
 */
@Tag(name = "40-人事服务-人事报表")
@RestController
@RequestMapping("/api/hrservice/report")
public class HrReportController {

    private final EmployeeService employeeService;

    public HrReportController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Operation(summary = "人事总览统计")
    @GetMapping("/overview")
    public Result<Map<String, Object>> overview() {
        List<Employee> employees = employeeService.list();

        Map<String, Object> result = new HashMap<>();
        result.put("totalEmployees", employees.size());
        result.put("onJob", countByStatus(employees, 1));
        result.put("probation", countByStatus(employees, 2));
        result.put("resigned", countByStatus(employees, 3));
        result.put("entryThisYear", countEntryThisYear(employees));
        result.put("deptStats", groupStats(employees, Employee::getDeptName));
        result.put("educationStats", groupStats(employees, Employee::getEducation));
        return Result.ok(result);
    }

    private long countByStatus(List<Employee> employees, int status) {
        return employees.stream()
                .filter(e -> e.getEmploymentStatus() != null && e.getEmploymentStatus() == status)
                .count();
    }

    private long countEntryThisYear(List<Employee> employees) {
        int year = LocalDate.now().getYear();
        return employees.stream()
                .filter(e -> e.getEntryDate() != null && e.getEntryDate().getYear() == year)
                .count();
    }

    /** 按指定字段分组计数，返回 [{name, count}]，按人数降序 */
    private List<Map<String, Object>> groupStats(List<Employee> employees, Function<Employee, String> keyGetter) {
        Map<String, Long> grouped = employees.stream()
                .filter(e -> StringUtils.hasText(keyGetter.apply(e)))
                .collect(Collectors.groupingBy(keyGetter, Collectors.counting()));
        return grouped.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .map(entry -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("name", entry.getKey());
                    item.put("count", entry.getValue());
                    return item;
                })
                .collect(Collectors.toList());
    }
}
