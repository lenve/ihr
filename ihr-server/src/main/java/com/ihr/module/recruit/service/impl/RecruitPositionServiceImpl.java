package com.ihr.module.recruit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ihr.module.recruit.entity.RecruitPosition;
import com.ihr.module.recruit.mapper.RecruitPositionMapper;
import com.ihr.module.recruit.service.RecruitPositionService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 招聘职位服务实现。
 */
@Service
public class RecruitPositionServiceImpl extends ServiceImpl<RecruitPositionMapper, RecruitPosition> implements RecruitPositionService {

    @Override
    public IPage<RecruitPosition> page(long pageNum, long pageSize, String keyword, Integer status) {
        LambdaQueryWrapper<RecruitPosition> wrapper = new LambdaQueryWrapper<RecruitPosition>()
                .like(StringUtils.hasText(keyword), RecruitPosition::getTitle, keyword)
                .eq(status != null, RecruitPosition::getStatus, status)
                .orderByDesc(RecruitPosition::getPublishTime);
        return page(Page.of(pageNum, pageSize), wrapper);
    }
}
