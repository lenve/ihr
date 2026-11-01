package com.ihr.module.performance.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 绩效目标实体（perf_goal），含自评与上级评分。
 */
@Data
@TableName("perf_goal")
public class PerfGoal {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long appraisalId;

    /** 目标名称 */
    private String goalName;

    /** 维度：业绩/行为/协作 */
    private String dimension;

    /** 权重% */
    private Integer weight;

    /** 目标值 */
    private String targetValue;

    /** 评分标准 */
    private String standard;

    /** 自评得分 */
    private BigDecimal selfScore;

    private String selfComment;

    /** 上级评分 */
    private BigDecimal superiorScore;

    private String superiorComment;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
