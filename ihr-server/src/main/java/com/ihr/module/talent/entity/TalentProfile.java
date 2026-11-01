package com.ihr.module.talent.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 人才库实体（talent_profile）。
 */
@Data
@TableName("talent_profile")
public class TalentProfile {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String gender;

    private String phone;

    private String email;

    /** 来源渠道 */
    private String source;

    /** 应聘职位 */
    private String applyPosition;

    private String education;

    /** 工作年限 */
    private Integer workYears;

    private String skill;

    /** 人才评级 S/A/B/C */
    private String evaluation;

    /** 状态：0待联系 1面试中 2已录用 3已淘汰 */
    private Integer status;

    private String resumeUrl;

    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
