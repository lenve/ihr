package com.ihr.module.workbench.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 待办通知实体。
 */
@Data
@TableName("todo_item")
public class TodoItem {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;

    private String content;

    /** 业务类型：考核/审批/公告等 */
    private String bizType;

    /** 处理人 */
    private String handlerName;

    /** 0待办 1已完成 */
    private Integer status;

    /** 截止时间 */
    private LocalDateTime dueTime;

    private LocalDateTime finishTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
