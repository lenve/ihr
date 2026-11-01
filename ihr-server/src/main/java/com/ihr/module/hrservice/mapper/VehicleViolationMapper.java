package com.ihr.module.hrservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ihr.module.hrservice.entity.VehicleViolation;
import org.apache.ibatis.annotations.Mapper;

/**
 * 车辆违规 Mapper。
 */
@Mapper
public interface VehicleViolationMapper extends BaseMapper<VehicleViolation> {
}
