package com.ihr.module.declare.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 职称申报实体（title_declaration）。
 */
@Data
@TableName("title_declaration")
public class TitleDeclaration {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String applicantName;

    private String deptName;

    /** 申报职称 */
    private String declareTitle;

    /** 初级/中级/副高/正高 */
    private String declareLevel;

    /** 专业 */
    private String major;

    /** 取得时间 */
    private LocalDate obtainDate;

    private String materialUrl;

    /** 0待审核 1通过 2驳回 */
    private Integer status;

    private String auditComment;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
