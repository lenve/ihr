package com.ihr.module.performance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ihr.common.exception.BusinessException;
import com.ihr.module.performance.entity.PerfAchievement;
import com.ihr.module.performance.mapper.PerfAchievementMapper;
import com.ihr.module.performance.service.PerfAchievementService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 业绩记录与团队业绩申请服务实现。
 */
@Service
public class PerfAchievementServiceImpl extends ServiceImpl<PerfAchievementMapper, PerfAchievement> implements PerfAchievementService {

    @Override
    public IPage<PerfAchievement> page(long pageNum, long pageSize, String keyword, Integer achieveType, Integer status) {
        LambdaQueryWrapper<PerfAchievement> wrapper = new LambdaQueryWrapper<PerfAchievement>()
                .like(StringUtils.hasText(keyword), PerfAchievement::getTitle, keyword)
                .eq(achieveType != null, PerfAchievement::getAchieveType, achieveType)
                .eq(status != null, PerfAchievement::getStatus, status)
                .orderByDesc(PerfAchievement::getId);
        return page(Page.of(pageNum, pageSize), wrapper);
    }

    @Override
    public void submit(Long id) {
        PerfAchievement achievement = requireAchievement(id);
        if (achievement.getStatus() == null || achievement.getStatus() != 0) {
            throw new BusinessException("仅草稿状态可提交");
        }
        achievement.setStatus(1);
        updateById(achievement);
    }

    @Override
    public void confirm(Long id, String auditComment) {
        PerfAchievement achievement = requireAchievement(id);
        if (achievement.getStatus() == null || achievement.getStatus() != 1) {
            throw new BusinessException("仅已提交状态可确认");
        }
        achievement.setStatus(2);
        achievement.setAuditComment(auditComment);
        updateById(achievement);
    }

    @Override
    public void reject(Long id, String auditComment) {
        PerfAchievement achievement = requireAchievement(id);
        if (achievement.getStatus() == null || achievement.getStatus() != 1) {
            throw new BusinessException("仅已提交状态可驳回");
        }
        achievement.setStatus(3);
        achievement.setAuditComment(auditComment);
        updateById(achievement);
    }

    private PerfAchievement requireAchievement(Long id) {
        PerfAchievement achievement = getById(id);
        if (achievement == null) {
            throw new BusinessException("业绩记录不存在");
        }
        return achievement;
    }
}
