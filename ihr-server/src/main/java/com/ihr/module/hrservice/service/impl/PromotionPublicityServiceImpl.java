package com.ihr.module.hrservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ihr.common.exception.BusinessException;
import com.ihr.module.hrservice.entity.PromotionPublicity;
import com.ihr.module.hrservice.mapper.PromotionPublicityMapper;
import com.ihr.module.hrservice.service.PromotionPublicityService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 晋升提拔公示服务实现。
 */
@Service
public class PromotionPublicityServiceImpl extends ServiceImpl<PromotionPublicityMapper, PromotionPublicity> implements PromotionPublicityService {

    @Override
    public IPage<PromotionPublicity> page(long pageNum, long pageSize, String keyword, Integer status) {
        LambdaQueryWrapper<PromotionPublicity> wrapper = new LambdaQueryWrapper<PromotionPublicity>()
                .like(StringUtils.hasText(keyword), PromotionPublicity::getEmpName, keyword)
                .eq(status != null, PromotionPublicity::getStatus, status)
                .orderByDesc(PromotionPublicity::getPublicityStart)
                .orderByDesc(PromotionPublicity::getId);
        return page(Page.of(pageNum, pageSize), wrapper);
    }

    @Override
    public void pass(Long id) {
        PromotionPublicity publicity = getById(id);
        if (publicity == null) {
            throw new BusinessException("公示记录不存在");
        }
        publicity.setStatus(1);
        updateById(publicity);
    }

    @Override
    public void markObjection(Long id) {
        PromotionPublicity publicity = getById(id);
        if (publicity == null) {
            throw new BusinessException("公示记录不存在");
        }
        publicity.setStatus(2);
        updateById(publicity);
    }
}
