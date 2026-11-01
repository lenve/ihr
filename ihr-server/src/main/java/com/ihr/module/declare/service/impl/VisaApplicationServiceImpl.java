package com.ihr.module.declare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ihr.common.exception.BusinessException;
import com.ihr.module.declare.entity.VisaApplication;
import com.ihr.module.declare.mapper.VisaApplicationMapper;
import com.ihr.module.declare.service.VisaApplicationService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 签证申请服务实现。
 */
@Service
public class VisaApplicationServiceImpl extends ServiceImpl<VisaApplicationMapper, VisaApplication> implements VisaApplicationService {

    @Override
    public IPage<VisaApplication> page(long pageNum, long pageSize, String keyword, Integer status) {
        LambdaQueryWrapper<VisaApplication> wrapper = new LambdaQueryWrapper<VisaApplication>()
                .like(StringUtils.hasText(keyword), VisaApplication::getApplicantName, keyword)
                .eq(status != null, VisaApplication::getStatus, status)
                .orderByDesc(VisaApplication::getId);
        return page(Page.of(pageNum, pageSize), wrapper);
    }

    @Override
    public void approve(Long id) {
        VisaApplication application = getById(id);
        if (application == null) {
            throw new BusinessException("签证申请不存在");
        }
        application.setStatus(1);
        updateById(application);
    }

    @Override
    public void reject(Long id) {
        VisaApplication application = getById(id);
        if (application == null) {
            throw new BusinessException("签证申请不存在");
        }
        application.setStatus(2);
        updateById(application);
    }

    @Override
    public void done(Long id) {
        VisaApplication application = getById(id);
        if (application == null) {
            throw new BusinessException("签证申请不存在");
        }
        application.setStatus(3);
        updateById(application);
    }
}
