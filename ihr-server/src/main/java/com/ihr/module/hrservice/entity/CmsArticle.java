package com.ihr.module.hrservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 内容文章实体（cms_article）：政策解读/办事指南/出入境指南。
 */
@Data
@TableName("cms_article")
public class CmsArticle {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** policy政策解读/guide办事指南/exit_entry出入境指南 */
    private String category;

    private String title;

    private String summary;

    private String content;

    private String author;

    private Integer viewCount;

    private LocalDateTime publishTime;

    /** 0下线 1上线 */
    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
