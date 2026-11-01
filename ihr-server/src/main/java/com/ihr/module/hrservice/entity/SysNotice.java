package com.ihr.module.hrservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 通知公告实体（sys_notice）。
 */
@Data
@TableName("sys_notice")
public class SysNotice {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;

    /** 1通知 2公告 3制度 */
    private Integer noticeType;

    private String content;

    private String publisher;

    private LocalDateTime publishTime;

    /** 0草稿 1已发布 2已下线 */
    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
