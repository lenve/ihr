package com.ihr.module.recruit.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 招聘职位实体（recruit_position）。
 */
@Data
@TableName("recruit_position")
public class RecruitPosition {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 职位名称 */
    private String title;

    private String deptName;

    /** 招聘人数 */
    private Integer headcount;

    private Integer salaryMin;

    private Integer salaryMax;

    private String education;

    /** 经验要求 */
    private String experience;

    /** 职位描述 */
    private String jobDesc;

    /** 任职要求 */
    private String requirement;

    /** 状态：0招聘中 1暂停 2关闭 */
    private Integer status;

    private String publisher;

    private LocalDateTime publishTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
