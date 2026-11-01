package com.ihr.module.recruit.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ihr.module.recruit.entity.RecruitPosition;

/**
 * 招聘职位服务。
 */
public interface RecruitPositionService extends IService<RecruitPosition> {

    /** 分页查询：支持关键字（职位名称模糊）与状态过滤 */
    IPage<RecruitPosition> page(long pageNum, long pageSize, String keyword, Integer status);
}
