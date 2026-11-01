package com.ihr.module.performance.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 考核关系实体（perf_relation）。
 */
@Data
@TableName("perf_relation")
public class PerfRelation {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 考核周期，如 2026-H2 */
    private String period;

    /** 被考核人 */
    private String empName;

    private String deptName;

    /** 考核人 */
    private String superiorName;

    /** 1上级 2同级 3下级 4HRBP */
    private Integer relationType;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
