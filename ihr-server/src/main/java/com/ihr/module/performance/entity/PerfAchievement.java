package com.ihr.module.performance.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 业绩记录与团队业绩申请实体（perf_achievement）。
 */
@Data
@TableName("perf_achievement")
public class PerfAchievement {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 1个人业绩 2团队业绩 */
    private Integer achieveType;

    private String period;

    /** 申请人 */
    private String empName;

    private String deptName;

    /** 业绩名称 */
    private String title;

    private String description;

    /** 业绩金额/量化值 */
    private BigDecimal amount;

    /** 0草稿 1已提交 2已确认 3已驳回 */
    private Integer status;

    private String auditComment;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
