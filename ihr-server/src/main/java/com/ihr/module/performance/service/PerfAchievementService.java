package com.ihr.module.performance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ihr.module.performance.entity.PerfAchievement;

/**
 * 业绩记录与团队业绩申请服务。
 */
public interface PerfAchievementService extends IService<PerfAchievement> {

    /** 分页查询：keyword 模糊匹配 title，achieveType/status 精确 */
    IPage<PerfAchievement> page(long pageNum, long pageSize, String keyword, Integer achieveType, Integer status);

    /** 提交：status 0→1 */
    void submit(Long id);

    /** 确认：status 1→2 */
    void confirm(Long id, String auditComment);

    /** 驳回：status 1→3 */
    void reject(Long id, String auditComment);
}
