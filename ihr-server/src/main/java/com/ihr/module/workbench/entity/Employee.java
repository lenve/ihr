package com.ihr.module.workbench.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 员工档案实体（emp_employee）。
 */
@Data
@TableName("emp_employee")
public class Employee {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 关联登录账号 */
    private String username;

    /** 工号 */
    private String empNo;

    private String realName;

    private String gender;

    private LocalDate birthDate;

    private String idCard;

    private String phone;

    private String email;

    /** 部门 */
    private String deptName;

    /** 岗位 */
    private String position;

    /** 职级 */
    private String jobLevel;

    private String education;

    private String school;

    private String major;

    /** 入职日期 */
    private LocalDate entryDate;

    /** 1在职 2试用 3离职 */
    private Integer employmentStatus;

    private String workPlace;

    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
