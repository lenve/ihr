package com.ihr.module.declare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ihr.module.declare.entity.TitleRecord;
import com.ihr.module.declare.mapper.TitleRecordMapper;
import com.ihr.module.declare.service.TitleRecordService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 职称记录服务实现。
 */
@Service
public class TitleRecordServiceImpl extends ServiceImpl<TitleRecordMapper, TitleRecord> implements TitleRecordService {

    @Override
    public IPage<TitleRecord> page(long pageNum, long pageSize, String keyword, Integer status) {
        LambdaQueryWrapper<TitleRecord> wrapper = new LambdaQueryWrapper<TitleRecord>()
                .like(StringUtils.hasText(keyword), TitleRecord::getEmpName, keyword)
                .eq(status != null, TitleRecord::getStatus, status)
                .orderByDesc(TitleRecord::getObtainedDate)
                .orderByDesc(TitleRecord::getId);
        return page(Page.of(pageNum, pageSize), wrapper);
    }
}
