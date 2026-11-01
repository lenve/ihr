package com.ihr.module.workbench.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 预警通知实体（alert_notice）。
 */
@Data
@TableName("alert_notice")
public class AlertNotice {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 级别：1提示 2警告 3严重 */
    private Integer level;

    private String title;

    private String content;

    /** 来源系统/模块 */
    private String source;

    /** 状态：0未处理 1已处理 */
    private Integer status;

    /** 处理备注 */
    private String handleRemark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
