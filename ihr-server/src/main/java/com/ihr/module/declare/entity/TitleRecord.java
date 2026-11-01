package com.ihr.module.declare.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 职称记录实体（title_record，我的职称）。
 */
@Data
@TableName("title_record")
public class TitleRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String empName;

    /** 职称名称 */
    private String titleName;

    /** 级别 */
    private String titleLevel;

    /** 证书编号 */
    private String certificateNo;

    /** 取得日期 */
    private LocalDate obtainedDate;

    /** 有效期至 */
    private LocalDate validityEnd;

    /** 1有效 0失效 */
    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
