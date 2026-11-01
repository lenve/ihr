package com.ihr.module.performance.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 绩效申诉实体（perf_appeal）。
 */
@Data
@TableName("perf_appeal")
public class PerfAppeal {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long appraisalId;

    private String empName;

    /** 申诉理由 */
    private String reason;

    /** 期望结果 */
    private String expect;

    /** 0申诉中 1已受理 2已驳回 3已调整 */
    private Integer status;

    private String auditComment;

    private LocalDateTime auditTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
