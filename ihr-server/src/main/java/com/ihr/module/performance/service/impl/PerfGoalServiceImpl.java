package com.ihr.module.performance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ihr.common.exception.BusinessException;
import com.ihr.module.performance.entity.PerfAppraisal;
import com.ihr.module.performance.entity.PerfGoal;
import com.ihr.module.performance.mapper.PerfAppraisalMapper;
import com.ihr.module.performance.mapper.PerfGoalMapper;
import com.ihr.module.performance.service.PerfGoalService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 绩效目标服务实现。
 */
@Service
public class PerfGoalServiceImpl extends ServiceImpl<PerfGoalMapper, PerfGoal> implements PerfGoalService {

    private final PerfAppraisalMapper perfAppraisalMapper;

    public PerfGoalServiceImpl(PerfAppraisalMapper perfAppraisalMapper) {
        this.perfAppraisalMapper = perfAppraisalMapper;
    }

    @Override
    public List<PerfGoal> listByAppraisal(Long appraisalId) {
        if (appraisalId == null) {
            throw new BusinessException("考核 ID 不能为空");
        }
        return list(new LambdaQueryWrapper<PerfGoal>()
                .eq(PerfGoal::getAppraisalId, appraisalId)
                .orderByAsc(PerfGoal::getId));
    }

    @Override
    public IPage<PerfGoal> page(long pageNum, long pageSize, String keyword, Long appraisalId) {
        LambdaQueryWrapper<PerfGoal> wrapper = new LambdaQueryWrapper<PerfGoal>()
                .like(StringUtils.hasText(keyword), PerfGoal::getGoalName, keyword)
                .eq(appraisalId != null, PerfGoal::getAppraisalId, appraisalId)
                .orderByDesc(PerfGoal::getId);
        return page(Page.of(pageNum, pageSize), wrapper);
    }

    @Override
    public void updateGoal(PerfGoal goal) {
        if (goal.getId() == null) {
            throw new BusinessException("目标 ID 不能为空");
        }
        PerfGoal exists = getById(goal.getId());
        if (exists == null) {
            throw new BusinessException("目标不存在");
        }
        updateById(goal);
        // 员工开始填写自评分时，考核由"目标确认"进入"自评中"
        if (goal.getSelfScore() != null && exists.getAppraisalId() != null) {
            PerfAppraisal appraisal = perfAppraisalMapper.selectById(exists.getAppraisalId());
            if (appraisal != null && appraisal.getStatus() != null
                    && appraisal.getStatus() == PerfAppraisal.STATUS_GOAL_CONFIRMED) {
                appraisal.setStatus(PerfAppraisal.STATUS_SELF_REVIEWING);
                perfAppraisalMapper.updateById(appraisal);
            }
        }
    }
}
