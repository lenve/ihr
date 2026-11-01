package com.ihr.module.hrservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 车辆违规实体（vehicle_violation）。
 */
@Data
@TableName("vehicle_violation")
public class VehicleViolation {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 车牌号 */
    private String plateNo;

    private String driverName;

    private LocalDateTime violateTime;

    private String violateType;

    private String location;

    private BigDecimal fine;

    /** 扣分 */
    private Integer points;

    /** 0未处理 1已处理 */
    private Integer deductStatus;

    private String handleRemark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
