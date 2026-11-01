package com.ihr.module.workbench.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 工作任务实体（work_task）。
 */
@Data
@TableName("work_task")
public class WorkTask {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;

    private String description;

    /** 派发人 */
    private String assignerName;

    /** 负责人 */
    private String assigneeName;

    /** 优先级：1高 2中 3低 */
    private Integer priority;

    /** 状态：0进行中 1已完成 2已取消 */
    private Integer status;

    /** 进度 0-100 */
    private Integer progress;

    /** 截止日期 */
    private LocalDate deadline;

    private LocalDateTime finishTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
