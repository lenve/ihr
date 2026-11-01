package com.ihr.module.talent.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 人才申报实体（talent_declaration）。
 */
@Data
@TableName("talent_declaration")
public class TalentDeclaration {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 申报人 */
    private String declarerName;

    /** 申报类型：内部推荐/高端人才/团队引进 */
    private String declareType;

    /** 被申报人 */
    private String personName;

    /** 申报理由 */
    private String reason;

    /** 材料地址 */
    private String materialUrl;

    /** 状态：0待审核 1通过 2驳回 */
    private Integer status;

    /** 审核意见 */
    private String auditComment;

    /** 审核人 */
    private String auditor;

    private LocalDateTime auditTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
