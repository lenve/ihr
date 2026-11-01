package com.ihr.module.talent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ihr.module.talent.entity.HrPool;
import com.ihr.module.talent.mapper.HrPoolMapper;
import com.ihr.module.talent.service.HrPoolService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 人力资源池服务实现。
 */
@Service
public class HrPoolServiceImpl extends ServiceImpl<HrPoolMapper, HrPool> implements HrPoolService {

    @Override
    public IPage<HrPool> page(long pageNum, long pageSize, String keyword, Integer status, Integer poolType) {
        LambdaQueryWrapper<HrPool> wrapper = new LambdaQueryWrapper<HrPool>()
                .like(StringUtils.hasText(keyword), HrPool::getName, keyword)
                .eq(status != null, HrPool::getStatus, status)
                .eq(poolType != null, HrPool::getPoolType, poolType)
                .orderByAsc(HrPool::getStatus)
                .orderByAsc(HrPool::getPoolType);
        return page(Page.of(pageNum, pageSize), wrapper);
    }
}
