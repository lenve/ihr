package com.ihr.module.talent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ihr.module.talent.entity.TalentProfile;
import com.ihr.module.talent.mapper.TalentProfileMapper;
import com.ihr.module.talent.service.TalentProfileService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 人才库服务实现。
 */
@Service
public class TalentProfileServiceImpl extends ServiceImpl<TalentProfileMapper, TalentProfile> implements TalentProfileService {

    @Override
    public IPage<TalentProfile> page(long pageNum, long pageSize, String keyword, Integer status) {
        LambdaQueryWrapper<TalentProfile> wrapper = new LambdaQueryWrapper<TalentProfile>()
                .like(StringUtils.hasText(keyword), TalentProfile::getName, keyword)
                .eq(status != null, TalentProfile::getStatus, status)
                .orderByAsc(TalentProfile::getStatus)
                .orderByDesc(TalentProfile::getCreateTime);
        return page(Page.of(pageNum, pageSize), wrapper);
    }
}
