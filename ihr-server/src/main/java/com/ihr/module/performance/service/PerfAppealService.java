package com.ihr.module.performance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ihr.module.performance.dto.AppealAuditDTO;
import com.ihr.module.performance.entity.PerfAppeal;

/**
 * 绩效申诉服务。
 */
public interface PerfAppealService extends IService<PerfAppeal> {

    /** 分页查询：keyword 模糊匹配 emp_name，status/appraisalId 精确 */
    IPage<PerfAppeal> page(long pageNum, long pageSize, String keyword, Integer status, Long appraisalId);

    /** 创建申诉（未填 emp_name 时取当前登录人），并把对应考核 status→8 */
    void createAppeal(PerfAppeal appeal);

    /** 审核申诉：status 1已受理/2已驳回/3已调整；驳回或调整后考核回 7 */
    void audit(Long id, AppealAuditDTO dto);
}
