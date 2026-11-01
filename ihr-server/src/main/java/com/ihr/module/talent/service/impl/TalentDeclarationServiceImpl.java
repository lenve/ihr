package com.ihr.module.talent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ihr.common.exception.BusinessException;
import com.ihr.module.talent.entity.TalentDeclaration;
import com.ihr.module.talent.mapper.TalentDeclarationMapper;
import com.ihr.module.talent.service.TalentDeclarationService;
import com.ihr.module.workbench.service.EmployeeService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * 人才申报服务实现。
 */
@Service
public class TalentDeclarationServiceImpl extends ServiceImpl<TalentDeclarationMapper, TalentDeclaration> implements TalentDeclarationService {

    private final EmployeeService employeeService;

    public TalentDeclarationServiceImpl(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public IPage<TalentDeclaration> page(long pageNum, long pageSize, String keyword, Integer status) {
        LambdaQueryWrapper<TalentDeclaration> wrapper = new LambdaQueryWrapper<TalentDeclaration>()
                .like(StringUtils.hasText(keyword), TalentDeclaration::getPersonName, keyword)
                .eq(status != null, TalentDeclaration::getStatus, status)
                .orderByAsc(TalentDeclaration::getStatus)
                .orderByDesc(TalentDeclaration::getCreateTime);
        return page(Page.of(pageNum, pageSize), wrapper);
    }

    @Override
    public void pass(Long id) {
        audit(id, true);
    }

    @Override
    public void reject(Long id) {
        audit(id, false);
    }

    private void audit(Long id, boolean pass) {
        TalentDeclaration declaration = getById(id);
        if (declaration == null) {
            throw new BusinessException("申报记录不存在");
        }
        declaration.setStatus(pass ? 1 : 2);
        declaration.setAuditComment(pass ? "审核通过" : "审核驳回");
        declaration.setAuditor(employeeService.currentRealName());
        declaration.setAuditTime(LocalDateTime.now());
        updateById(declaration);
    }
}
