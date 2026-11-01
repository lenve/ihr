package com.ihr.module.hrservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ihr.module.hrservice.entity.TrainingRecord;
import com.ihr.module.hrservice.mapper.TrainingRecordMapper;
import com.ihr.module.hrservice.service.TrainingRecordService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 培训档案服务实现。
 */
@Service
public class TrainingRecordServiceImpl extends ServiceImpl<TrainingRecordMapper, TrainingRecord> implements TrainingRecordService {

    @Override
    public IPage<TrainingRecord> page(long pageNum, long pageSize, String keyword) {
        LambdaQueryWrapper<TrainingRecord> wrapper = new LambdaQueryWrapper<TrainingRecord>()
                .and(StringUtils.hasText(keyword), w -> w.like(TrainingRecord::getEmpName, keyword)
                        .or()
                        .like(TrainingRecord::getCourseName, keyword))
                .orderByDesc(TrainingRecord::getStartDate)
                .orderByDesc(TrainingRecord::getId);
        return page(Page.of(pageNum, pageSize), wrapper);
    }
}
