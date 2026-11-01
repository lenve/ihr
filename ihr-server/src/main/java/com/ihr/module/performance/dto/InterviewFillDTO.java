package com.ihr.module.performance.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 面谈填写入参。
 */
@Data
public class InterviewFillDTO {

    private LocalDateTime interviewTime;

    /** 优势肯定 */
    private String advantage;

    /** 不足指出 */
    private String shortage;

    /** 改进计划 */
    private String improvePlan;
}
