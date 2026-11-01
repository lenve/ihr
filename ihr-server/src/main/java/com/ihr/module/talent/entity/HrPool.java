package com.ihr.module.talent.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 人力资源池实体（hr_pool）。
 */
@Data
@TableName("hr_pool")
public class HrPool {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 人员姓名 */
    private String name;

    private String deptName;

    private String position;

    /** 技能标签 */
    private String skill;

    /** 类型：1内部 2外部 */
    private Integer poolType;

    /** 可投入度说明 */
    private String availability;

    /** 状态：1可用 2已占用 */
    private Integer status;

    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
