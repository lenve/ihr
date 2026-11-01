package com.ihr.module.performance.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 绩效考核主表实体（perf_appraisal）。
 * 状态机：0目标制定→1目标确认→2自评中→3上级评价→4待面谈→5面谈审核→6待签署→7已完成→8申诉中。
 */
@Data
@TableName("perf_appraisal")
public class PerfAppraisal {

    public static final int STATUS_GOAL_SETTING = 0;
    public static final int STATUS_GOAL_CONFIRMED = 1;
    public static final int STATUS_SELF_REVIEWING = 2;
    public static final int STATUS_SUPERIOR_REVIEWING = 3;
    public static final int STATUS_INTERVIEW_WAITING = 4;
    public static final int STATUS_INTERVIEW_AUDITING = 5;
    public static final int STATUS_SIGNING = 6;
    public static final int STATUS_FINISHED = 7;
    public static final int STATUS_APPEALING = 8;

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 考核周期 */
    private String period;

    /** 被考核人 */
    private String empName;

    private String deptName;

    /** 考核人 */
    private String superiorName;

    private Integer status;

    /** 自评总分 */
    private BigDecimal selfScore;

    /** 上级评分 */
    private BigDecimal superiorScore;

    /** 综合得分=自评40%+上级60% */
    private BigDecimal totalScore;

    /** 等级 S/A/B/C/D */
    private String grade;

    /** 0未签署 1已签署 */
    private Integer signStatus;

    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
