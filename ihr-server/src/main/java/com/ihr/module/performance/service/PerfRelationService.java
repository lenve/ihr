package com.ihr.module.performance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ihr.module.performance.entity.PerfRelation;

/**
 * 考核关系服务。
 */
public interface PerfRelationService extends IService<PerfRelation> {

    /** 分页查询：keyword 模糊匹配 emp_name，period 精确 */
    IPage<PerfRelation> page(long pageNum, long pageSize, String keyword, String period);
}
