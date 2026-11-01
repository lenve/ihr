package com.ihr.module.hrservice.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ihr.module.hrservice.entity.SalaryRecord;

/**
 * 工资记录服务。
 */
public interface SalaryRecordService extends IService<SalaryRecord> {

    /** 分页查询：关键字（姓名模糊）+ 工资月份 + 状态 */
    IPage<SalaryRecord> page(long pageNum, long pageSize, String keyword, String salaryMonth, Integer status);
}
