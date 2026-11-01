package com.ihr.module.performance.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 申诉审核入参。
 */
@Data
public class AppealAuditDTO {

    /** 审核结果：1已受理 2已驳回 3已调整 */
    private Integer status;

    private String auditComment;

    private LocalDateTime auditTime;
}
