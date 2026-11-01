package com.ihr.module.performance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ihr.module.performance.entity.PerfAppraisal;

import java.util.Map;

/**
 * 绩效考核主表服务（含流程状态机动作）。
 */
public interface PerfAppraisalService extends IService<PerfAppraisal> {

    /** 分页查询：keyword 模糊匹配 emp_name，period/status 精确 */
    IPage<PerfAppraisal> page(long pageNum, long pageSize, String keyword, String period, Integer status);

    /** 确认目标：status 0→1 */
    void confirmGoal(Long id);

    /** 提交自评：校验所有目标已自评，计算自评总分，status 1/2→3 */
    void submitSelfReview(Long id);

    /** 提交上级评价：校验所有目标已有上级评分，计算上级总分/综合分/等级，status 3→4 */
    void submitSuperiorReview(Long id);

    /** 面谈审核通过：status 5→6 */
    void interviewPassed(Long id);

    /** 签署：signStatus=1，status 6→7 */
    void sign(Long id);

    /** 考核详情：考核 + 目标列表 + 面谈记录 */
    Map<String, Object> detail(Long id);
}
