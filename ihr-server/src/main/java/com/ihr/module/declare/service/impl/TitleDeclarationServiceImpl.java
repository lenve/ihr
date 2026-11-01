package com.ihr.module.declare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ihr.common.exception.BusinessException;
import com.ihr.module.declare.entity.TitleDeclaration;
import com.ihr.module.declare.mapper.TitleDeclarationMapper;
import com.ihr.module.declare.service.TitleDeclarationService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 职称申报服务实现。
 */
@Service
public class TitleDeclarationServiceImpl extends ServiceImpl<TitleDeclarationMapper, TitleDeclaration> implements TitleDeclarationService {

    @Override
    public IPage<TitleDeclaration> page(long pageNum, long pageSize, String keyword, Integer status) {
        LambdaQueryWrapper<TitleDeclaration> wrapper = new LambdaQueryWrapper<TitleDeclaration>()
                .like(StringUtils.hasText(keyword), TitleDeclaration::getApplicantName, keyword)
                .eq(status != null, TitleDeclaration::getStatus, status)
                .orderByDesc(TitleDeclaration::getId);
        return page(Page.of(pageNum, pageSize), wrapper);
    }

    @Override
    public void pass(Long id) {
        TitleDeclaration declaration = getById(id);
        if (declaration == null) {
            throw new BusinessException("职称申报不存在");
        }
        declaration.setStatus(1);
        updateById(declaration);
    }

    @Override
    public void reject(Long id) {
        TitleDeclaration declaration = getById(id);
        if (declaration == null) {
            throw new BusinessException("职称申报不存在");
        }
        declaration.setStatus(2);
        updateById(declaration);
    }
}
