package com.ihr.module.performance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ihr.common.exception.BusinessException;
import com.ihr.module.performance.entity.PerfAppraisal;
import com.ihr.module.performance.entity.PerfGoal;
import com.ihr.module.performance.entity.PerfInterview;
import com.ihr.module.performance.mapper.PerfAppraisalMapper;
import com.ihr.module.performance.mapper.PerfGoalMapper;
import com.ihr.module.performance.mapper.PerfInterviewMapper;
import com.ihr.module.performance.service.PerfAppraisalService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 绩效考核主表服务实现（含评分汇总与状态机流转）。
 */
@Service
public class PerfAppraisalServiceImpl extends ServiceImpl<PerfAppraisalMapper, PerfAppraisal> implements PerfAppraisalService {

    private final PerfGoalMapper perfGoalMapper;
    private final PerfInterviewMapper perfInterviewMapper;

    public PerfAppraisalServiceImpl(PerfGoalMapper perfGoalMapper, PerfInterviewMapper perfInterviewMapper) {
        this.perfGoalMapper = perfGoalMapper;
        this.perfInterviewMapper = perfInterviewMapper;
    }

    @Override
    public IPage<PerfAppraisal> page(long pageNum, long pageSize, String keyword, String period, Integer status) {
        LambdaQueryWrapper<PerfAppraisal> wrapper = new LambdaQueryWrapper<PerfAppraisal>()
                .like(StringUtils.hasText(keyword), PerfAppraisal::getEmpName, keyword)
                .eq(StringUtils.hasText(period), PerfAppraisal::getPeriod, period)
                .eq(status != null, PerfAppraisal::getStatus, status)
                .orderByDesc(PerfAppraisal::getId);
        return page(Page.of(pageNum, pageSize), wrapper);
    }

    @Override
    public void confirmGoal(Long id) {
        PerfAppraisal appraisal = requireAppraisal(id);
        if (appraisal.getStatus() == null || appraisal.getStatus() != PerfAppraisal.STATUS_GOAL_SETTING) {
            throw new BusinessException("当前状态不允许确认目标");
        }
        List<PerfGoal> goals = goalsOf(id);
        if (goals.isEmpty()) {
            throw new BusinessException("请先添加考核目标");
        }
        appraisal.setStatus(PerfAppraisal.STATUS_GOAL_CONFIRMED);
        updateById(appraisal);
    }

    @Override
    public void submitSelfReview(Long id) {
        PerfAppraisal appraisal = requireAppraisal(id);
        int status = appraisal.getStatus() == null ? -1 : appraisal.getStatus();
        if (status != PerfAppraisal.STATUS_GOAL_CONFIRMED && status != PerfAppraisal.STATUS_SELF_REVIEWING) {
            throw new BusinessException("当前状态不允许提交自评");
        }
        List<PerfGoal> goals = goalsOf(id);
        if (goals.isEmpty()) {
            throw new BusinessException("考核暂无目标");
        }
        for (PerfGoal goal : goals) {
            if (goal.getSelfScore() == null) {
                throw new BusinessException("目标「" + goal.getGoalName() + "」尚未自评");
            }
        }
        appraisal.setSelfScore(weightedScore(goals, true));
        appraisal.setStatus(PerfAppraisal.STATUS_SUPERIOR_REVIEWING);
        updateById(appraisal);
    }

    @Override
    public void submitSuperiorReview(Long id) {
        PerfAppraisal appraisal = requireAppraisal(id);
        int status = appraisal.getStatus() == null ? -1 : appraisal.getStatus();
        if (status != PerfAppraisal.STATUS_SUPERIOR_REVIEWING) {
            throw new BusinessException("当前状态不允许提交上级评价");
        }
        List<PerfGoal> goals = goalsOf(id);
        if (goals.isEmpty()) {
            throw new BusinessException("考核暂无目标");
        }
        for (PerfGoal goal : goals) {
            if (goal.getSuperiorScore() == null) {
                throw new BusinessException("目标「" + goal.getGoalName() + "」尚无上级评分");
            }
        }
        appraisal.setSuperiorScore(weightedScore(goals, false));
        BigDecimal self = appraisal.getSelfScore() == null ? BigDecimal.ZERO : appraisal.getSelfScore();
        appraisal.setTotalScore(self.multiply(new BigDecimal("0.4"))
                .add(appraisal.getSuperiorScore().multiply(new BigDecimal("0.6")))
                .setScale(2, RoundingMode.HALF_UP));
        appraisal.setGrade(gradeOf(appraisal.getTotalScore()));
        appraisal.setStatus(PerfAppraisal.STATUS_INTERVIEW_WAITING);
        updateById(appraisal);
    }

    @Override
    public void interviewPassed(Long id) {
        PerfAppraisal appraisal = requireAppraisal(id);
        int status = appraisal.getStatus() == null ? -1 : appraisal.getStatus();
        if (status != PerfAppraisal.STATUS_INTERVIEW_AUDITING) {
            throw new BusinessException("当前状态不允许面谈审核通过");
        }
        appraisal.setStatus(PerfAppraisal.STATUS_SIGNING);
        updateById(appraisal);
    }

    @Override
    public void sign(Long id) {
        PerfAppraisal appraisal = requireAppraisal(id);
        int status = appraisal.getStatus() == null ? -1 : appraisal.getStatus();
        if (status != PerfAppraisal.STATUS_SIGNING) {
            throw new BusinessException("当前状态不允许签署");
        }
        appraisal.setSignStatus(1);
        appraisal.setStatus(PerfAppraisal.STATUS_FINISHED);
        updateById(appraisal);
    }

    @Override
    public Map<String, Object> detail(Long id) {
        PerfAppraisal appraisal = requireAppraisal(id);
        Map<String, Object> result = new HashMap<>();
        result.put("appraisal", appraisal);
        result.put("goals", goalsOf(id));
        result.put("interviews", perfInterviewMapper.selectList(
                new LambdaQueryWrapper<PerfInterview>().eq(PerfInterview::getAppraisalId, id)));
        return result;
    }

    private PerfAppraisal requireAppraisal(Long id) {
        PerfAppraisal appraisal = getById(id);
        if (appraisal == null) {
            throw new BusinessException("考核记录不存在");
        }
        return appraisal;
    }

    private List<PerfGoal> goalsOf(Long appraisalId) {
        return perfGoalMapper.selectList(
                new LambdaQueryWrapper<PerfGoal>().eq(PerfGoal::getAppraisalId, appraisalId));
    }

    /** 加权总分：Σ(score×weight)/100，自评或上级 */
    private BigDecimal weightedScore(List<PerfGoal> goals, boolean self) {
        BigDecimal total = BigDecimal.ZERO;
        for (PerfGoal goal : goals) {
            BigDecimal score = self ? goal.getSelfScore() : goal.getSuperiorScore();
            int weight = goal.getWeight() == null ? 0 : goal.getWeight();
            total = total.add(score.multiply(BigDecimal.valueOf(weight)));
        }
        return total.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    /** 等级：≥90 S、≥80 A、≥70 B、≥60 C、否则 D */
    private String gradeOf(BigDecimal totalScore) {
        double v = totalScore.doubleValue();
        if (v >= 90) {
            return "S";
        }
        if (v >= 80) {
            return "A";
        }
        if (v >= 70) {
            return "B";
        }
        if (v >= 60) {
            return "C";
        }
        return "D";
    }
}
