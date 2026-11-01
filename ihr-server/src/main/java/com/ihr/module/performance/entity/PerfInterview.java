package com.ihr.module.performance.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 绩效面谈实体（perf_interview）。
 */
@Data
@TableName("perf_interview")
public class PerfInterview {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long appraisalId;

    private String empName;

    /** 面谈人 */
    private String interviewer;

    private LocalDateTime interviewTime;

    /** 优势肯定 */
    private String advantage;

    /** 不足指出 */
    private String shortage;

    /** 改进计划 */
    private String improvePlan;

    /** 0未填写 1已填写 */
    private Integer fillStatus;

    /** 0待审核 1通过 2驳回 */
    private Integer auditStatus;

    private String auditComment;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
