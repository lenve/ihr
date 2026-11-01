package com.ihr.module.performance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ihr.common.exception.BusinessException;
import com.ihr.module.performance.dto.InterviewFillDTO;
import com.ihr.module.performance.entity.PerfAppraisal;
import com.ihr.module.performance.entity.PerfInterview;
import com.ihr.module.performance.mapper.PerfAppraisalMapper;
import com.ihr.module.performance.mapper.PerfInterviewMapper;
import com.ihr.module.performance.service.PerfInterviewService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * 绩效面谈服务实现。
 */
@Service
public class PerfInterviewServiceImpl extends ServiceImpl<PerfInterviewMapper, PerfInterview> implements PerfInterviewService {

    private final PerfAppraisalMapper perfAppraisalMapper;

    public PerfInterviewServiceImpl(PerfAppraisalMapper perfAppraisalMapper) {
        this.perfAppraisalMapper = perfAppraisalMapper;
    }

    @Override
    public IPage<PerfInterview> page(long pageNum, long pageSize, String keyword, Integer fillStatus, Integer auditStatus) {
        LambdaQueryWrapper<PerfInterview> wrapper = new LambdaQueryWrapper<PerfInterview>()
                .like(StringUtils.hasText(keyword), PerfInterview::getEmpName, keyword)
                .eq(fillStatus != null, PerfInterview::getFillStatus, fillStatus)
                .eq(auditStatus != null, PerfInterview::getAuditStatus, auditStatus)
                .orderByDesc(PerfInterview::getId);
        return page(Page.of(pageNum, pageSize), wrapper);
    }

    @Override
    public void fill(Long id, InterviewFillDTO dto) {
        PerfInterview interview = requireInterview(id);
        if (interview.getFillStatus() != null && interview.getFillStatus() == 1) {
            throw new BusinessException("该面谈记录已填写");
        }
        interview.setInterviewTime(dto.getInterviewTime() != null ? dto.getInterviewTime() : LocalDateTime.now());
        interview.setAdvantage(dto.getAdvantage());
        interview.setShortage(dto.getShortage());
        interview.setImprovePlan(dto.getImprovePlan());
        interview.setFillStatus(1);
        updateById(interview);
        // 面谈填写完成后，对应考核由"待面谈"进入"面谈审核"
        moveAppraisal(interview.getAppraisalId(), PerfAppraisal.STATUS_INTERVIEW_WAITING,
                PerfAppraisal.STATUS_INTERVIEW_AUDITING, null);
    }

    @Override
    public void audit(Long id, boolean passed) {
        PerfInterview interview = requireInterview(id);
        if (interview.getFillStatus() == null || interview.getFillStatus() != 1) {
            throw new BusinessException("面谈记录尚未填写，无法审核");
        }
        if (interview.getAuditStatus() != null && interview.getAuditStatus() != 0) {
            throw new BusinessException("该面谈记录已审核");
        }
        interview.setAuditStatus(passed ? 1 : 2);
        updateById(interview);
        if (passed) {
            // 审核通过：考核 5→6 待签署
            moveAppraisal(interview.getAppraisalId(), PerfAppraisal.STATUS_INTERVIEW_AUDITING,
                    PerfAppraisal.STATUS_SIGNING, PerfAppraisal.STATUS_SIGNING);
        } else {
            // 驳回：考核回 4 待面谈（兼容已流转到 6 的情况一并回退）
            moveAppraisal(interview.getAppraisalId(), PerfAppraisal.STATUS_INTERVIEW_AUDITING,
                    PerfAppraisal.STATUS_INTERVIEW_WAITING, PerfAppraisal.STATUS_SIGNING);
        }
    }

    private PerfInterview requireInterview(Long id) {
        PerfInterview interview = getById(id);
        if (interview == null) {
            throw new BusinessException("面谈记录不存在");
        }
        return interview;
    }

    /** 考核状态流转：fromStatus（或 fallbackFrom）→ toStatus */
    private void moveAppraisal(Long appraisalId, int fromStatus, int toStatus, Integer fallbackFrom) {
        if (appraisalId == null) {
            return;
        }
        PerfAppraisal appraisal = perfAppraisalMapper.selectById(appraisalId);
        if (appraisal == null || appraisal.getStatus() == null) {
            return;
        }
        int status = appraisal.getStatus();
        if (status == fromStatus || (fallbackFrom != null && status == fallbackFrom)) {
            appraisal.setStatus(toStatus);
            perfAppraisalMapper.updateById(appraisal);
        }
    }
}
