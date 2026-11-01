package com.ihr.module.hrservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ihr.common.exception.BusinessException;
import com.ihr.module.hrservice.entity.VehicleViolation;
import com.ihr.module.hrservice.mapper.VehicleViolationMapper;
import com.ihr.module.hrservice.service.VehicleViolationService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 车辆违规服务实现。
 */
@Service
public class VehicleViolationServiceImpl extends ServiceImpl<VehicleViolationMapper, VehicleViolation> implements VehicleViolationService {

    @Override
    public IPage<VehicleViolation> page(long pageNum, long pageSize, String keyword, Integer deductStatus) {
        LambdaQueryWrapper<VehicleViolation> wrapper = new LambdaQueryWrapper<VehicleViolation>()
                .like(StringUtils.hasText(keyword), VehicleViolation::getPlateNo, keyword)
                .eq(deductStatus != null, VehicleViolation::getDeductStatus, deductStatus)
                .orderByDesc(VehicleViolation::getViolateTime)
                .orderByDesc(VehicleViolation::getId);
        return page(Page.of(pageNum, pageSize), wrapper);
    }

    @Override
    public void handle(Long id) {
        VehicleViolation violation = getById(id);
        if (violation == null) {
            throw new BusinessException("违规记录不存在");
        }
        violation.setDeductStatus(1);
        updateById(violation);
    }
}
