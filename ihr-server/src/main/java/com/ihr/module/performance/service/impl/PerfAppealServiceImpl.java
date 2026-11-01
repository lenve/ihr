package com.ihr.module.performance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ihr.common.exception.BusinessException;
import com.ihr.module.performance.dto.AppealAuditDTO;
import com.ihr.module.performance.entity.PerfAppeal;
import com.ihr.module.performance.entity.PerfAppraisal;
import com.ihr.module.performance.mapper.PerfAppealMapper;
import com.ihr.module.performance.mapper.PerfAppraisalMapper;
import com.ihr.module.performance.service.PerfAppealService;
import com.ihr.module.workbench.service.EmployeeService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * 绩效申诉服务实现。
 */
@Service
public class PerfAppealServiceImpl extends ServiceImpl<PerfAppealMapper, PerfAppeal> implements PerfAppealService {

    private final PerfAppraisalMapper perfAppraisalMapper;
    private final EmployeeService employeeService;

    public PerfAppealServiceImpl(PerfAppraisalMapper perfAppraisalMapper, EmployeeService employeeService) {
        this.perfAppraisalMapper = perfAppraisalMapper;
        this.employeeService = employeeService;
    }

    @Override
    public IPage<PerfAppeal> page(long pageNum, long pageSize, String keyword, Integer status, Long appraisalId) {
        LambdaQueryWrapper<PerfAppeal> wrapper = new LambdaQueryWrapper<PerfAppeal>()
                .like(StringUtils.hasText(keyword), PerfAppeal::getEmpName, keyword)
                .eq(status != null, PerfAppeal::getStatus, status)
                .eq(appraisalId != null, PerfAppeal::getAppraisalId, appraisalId)
                .orderByDesc(PerfAppeal::getId);
        return page(Page.of(pageNum, pageSize), wrapper);
    }

    @Override
    public void createAppeal(PerfAppeal appeal) {
        if (appeal.getAppraisalId() == null) {
            throw new BusinessException("缺少关联考核 ID");
        }
        if (!StringUtils.hasText(appeal.getReason())) {
            throw new BusinessException("请填写申诉理由");
        }
        PerfAppraisal appraisal = perfAppraisalMapper.selectById(appeal.getAppraisalId());
        if (appraisal == null) {
            throw new BusinessException("关联考核不存在");
        }
        if (!StringUtils.hasText(appeal.getEmpName())) {
            appeal.setEmpName(employeeService.currentRealName());
        }
        appeal.setId(null);
        appeal.setStatus(0);
        appeal.setAuditTime(null);
        save(appeal);
        // 发起申诉后考核进入"申诉中"
        appraisal.setStatus(PerfAppraisal.STATUS_APPEALING);
        perfAppraisalMapper.updateById(appraisal);
    }

    @Override
    public void audit(Long id, AppealAuditDTO dto) {
        PerfAppeal appeal = getById(id);
        if (appeal == null) {
            throw new BusinessException("申诉不存在");
        }
        if (dto.getStatus() == null || dto.getStatus() < 1 || dto.getStatus() > 3) {
            throw new BusinessException("审核结果不合法");
        }
        appeal.setStatus(dto.getStatus());
        appeal.setAuditComment(dto.getAuditComment());
        appeal.setAuditTime(dto.getAuditTime() != null ? dto.getAuditTime() : LocalDateTime.now());
        updateById(appeal);
        // 驳回/调整（处理完毕）后考核回到"已完成"
        if (dto.getStatus() == 2 || dto.getStatus() == 3) {
            PerfAppraisal appraisal = perfAppraisalMapper.selectById(appeal.getAppraisalId());
            if (appraisal != null && appraisal.getStatus() != null
                    && appraisal.getStatus() == PerfAppraisal.STATUS_APPEALING) {
                appraisal.setStatus(PerfAppraisal.STATUS_FINISHED);
                perfAppraisalMapper.updateById(appraisal);
            }
        }
    }
}
