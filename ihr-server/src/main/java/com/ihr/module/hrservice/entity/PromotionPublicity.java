package com.ihr.module.hrservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 晋升提拔公示实体（promotion_publicity）。
 */
@Data
@TableName("promotion_publicity")
public class PromotionPublicity {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String empName;

    private String deptName;

    /** 原岗位 */
    private String fromPosition;

    /** 拟任岗位 */
    private String toPosition;

    private String applyReason;

    private LocalDate publicityStart;

    private LocalDate publicityEnd;

    /** 0公示中 1已通过 2有异议 */
    private Integer status;

    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
