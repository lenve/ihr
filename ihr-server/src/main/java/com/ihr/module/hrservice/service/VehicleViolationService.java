package com.ihr.module.hrservice.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ihr.module.hrservice.entity.VehicleViolation;

/**
 * 车辆违规服务。
 */
public interface VehicleViolationService extends IService<VehicleViolation> {

    /** 分页查询：关键字（车牌模糊）+ 处理状态 */
    IPage<VehicleViolation> page(long pageNum, long pageSize, String keyword, Integer deductStatus);

    /** 处理（deductStatus=1） */
    void handle(Long id);
}
