package com.ihr.module.performance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ihr.module.performance.entity.PerfRelation;
import com.ihr.module.performance.mapper.PerfRelationMapper;
import com.ihr.module.performance.service.PerfRelationService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 考核关系服务实现。
 */
@Service
public class PerfRelationServiceImpl extends ServiceImpl<PerfRelationMapper, PerfRelation> implements PerfRelationService {

    @Override
    public IPage<PerfRelation> page(long pageNum, long pageSize, String keyword, String period) {
        LambdaQueryWrapper<PerfRelation> wrapper = new LambdaQueryWrapper<PerfRelation>()
                .like(StringUtils.hasText(keyword), PerfRelation::getEmpName, keyword)
                .eq(StringUtils.hasText(period), PerfRelation::getPeriod, period)
                .orderByDesc(PerfRelation::getId);
        return page(Page.of(pageNum, pageSize), wrapper);
    }
}
