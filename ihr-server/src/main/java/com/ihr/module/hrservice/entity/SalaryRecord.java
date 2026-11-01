package com.ihr.module.hrservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 工资记录实体（salary_record）。
 */
@Data
@TableName("salary_record")
public class SalaryRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String empName;

    private String deptName;

    /** 工资月份，如 2026-10 */
    private String salaryMonth;

    private BigDecimal baseSalary;

    private BigDecimal postSalary;

    private BigDecimal bonus;

    private BigDecimal deduction;

    /** 社保公积金 */
    private BigDecimal insurance;

    private BigDecimal tax;

    /** 实发合计 */
    private BigDecimal netSalary;

    /** 0未发放 1已发放 */
    private Integer status;

    private LocalDateTime payTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
