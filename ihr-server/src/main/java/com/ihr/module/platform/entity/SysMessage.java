package com.ihr.module.platform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 站内消息实体（sys_message）。
 */
@Data
@TableName("sys_message")
public class SysMessage {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 发件人 */
    private String senderName;

    /** 收件人 */
    private String receiverName;

    private String title;

    private String content;

    /** 0未读 1已读 */
    private Integer readFlag;

    private LocalDateTime createTime;
}
