package com.ihr.module.hrservice.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ihr.module.hrservice.entity.TrainingRecord;

/**
 * 培训档案服务。
 */
public interface TrainingRecordService extends IService<TrainingRecord> {

    /** 分页查询：关键字（姓名或课程名模糊） */
    IPage<TrainingRecord> page(long pageNum, long pageSize, String keyword);
}
