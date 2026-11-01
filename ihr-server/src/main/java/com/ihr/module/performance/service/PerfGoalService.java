package com.ihr.module.performance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ihr.module.performance.entity.PerfGoal;

import java.util.List;

/**
 * 绩效目标服务。
 */
public interface PerfGoalService extends IService<PerfGoal> {

    /** 按考核列出全部目标 */
    List<PerfGoal> listByAppraisal(Long appraisalId);

    /** 分页查询：keyword 模糊匹配 goal_name */
    IPage<PerfGoal> page(long pageNum, long pageSize, String keyword, Long appraisalId);

    /** 修改目标；若填写了自评分且考核处于"目标确认"，则考核进入"自评中" */
    void updateGoal(PerfGoal goal);
}
