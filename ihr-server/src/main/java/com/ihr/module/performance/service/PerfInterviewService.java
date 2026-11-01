package com.ihr.module.performance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ihr.module.performance.dto.InterviewFillDTO;
import com.ihr.module.performance.entity.PerfInterview;

/**
 * 绩效面谈服务。
 */
public interface PerfInterviewService extends IService<PerfInterview> {

    /** 分页查询：keyword 模糊匹配 emp_name，fillStatus/auditStatus 精确 */
    IPage<PerfInterview> page(long pageNum, long pageSize, String keyword, Integer fillStatus, Integer auditStatus);

    /** 填写面谈记录：fillStatus=1，并将考核 status 4→5 */
    void fill(Long id, InterviewFillDTO dto);

    /** 审核面谈记录：通过 auditStatus=1 且考核 5→6；驳回 auditStatus=2 且考核回 4 */
    void audit(Long id, boolean passed);
}
