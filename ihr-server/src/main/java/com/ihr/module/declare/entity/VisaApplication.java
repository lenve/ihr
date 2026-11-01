package com.ihr.module.declare.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 签证申请实体（visa_application）。
 */
@Data
@TableName("visa_application")
public class VisaApplication {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String applicantName;

    private String deptName;

    private String country;

    /** 签证类型 */
    private String visaType;

    /** 单次/多次 */
    private String entryCount;

    private LocalDate planDeparture;

    private LocalDate planReturn;

    private String reason;

    /** 0待审批 1已通过 2已驳回 3已办理 */
    private Integer status;

    private String auditComment;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
