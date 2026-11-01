package com.ihr.module.hrservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ihr.module.hrservice.entity.SalaryRecord;
import com.ihr.module.hrservice.mapper.SalaryRecordMapper;
import com.ihr.module.hrservice.service.SalaryRecordService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 工资记录服务实现。
 */
@Service
public class SalaryRecordServiceImpl extends ServiceImpl<SalaryRecordMapper, SalaryRecord> implements SalaryRecordService {

    @Override
    public IPage<SalaryRecord> page(long pageNum, long pageSize, String keyword, String salaryMonth, Integer status) {
        LambdaQueryWrapper<SalaryRecord> wrapper = new LambdaQueryWrapper<SalaryRecord>()
                .like(StringUtils.hasText(keyword), SalaryRecord::getEmpName, keyword)
                .eq(StringUtils.hasText(salaryMonth), SalaryRecord::getSalaryMonth, salaryMonth)
                .eq(status != null, SalaryRecord::getStatus, status)
                .orderByDesc(SalaryRecord::getSalaryMonth)
                .orderByDesc(SalaryRecord::getId);
        return page(Page.of(pageNum, pageSize), wrapper);
    }
}
