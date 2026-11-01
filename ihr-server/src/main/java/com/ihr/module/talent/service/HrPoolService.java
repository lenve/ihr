package com.ihr.module.talent.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ihr.module.talent.entity.HrPool;

/**
 * 人力资源池服务。
 */
public interface HrPoolService extends IService<HrPool> {

    /** 分页查询：支持关键字（姓名模糊）、状态与类型过滤 */
    IPage<HrPool> page(long pageNum, long pageSize, String keyword, Integer status, Integer poolType);
}
