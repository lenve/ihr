package com.ihr.module.hrservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 培训档案实体（training_record）。
 */
@Data
@TableName("training_record")
public class TrainingRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String empName;

    private String deptName;

    private String courseName;

    /** 培训分类 */
    private String category;

    private String trainer;

    private LocalDate startDate;

    private LocalDate endDate;

    /** 学时 */
    private Integer hours;

    /** 考核成绩 */
    private BigDecimal score;

    /** 证书 */
    private String certificate;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
