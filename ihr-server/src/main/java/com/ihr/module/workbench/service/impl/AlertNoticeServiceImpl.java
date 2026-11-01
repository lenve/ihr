package com.ihr.module.workbench.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ihr.common.exception.BusinessException;
import com.ihr.module.workbench.entity.AlertNotice;
import com.ihr.module.workbench.mapper.AlertNoticeMapper;
import com.ihr.module.workbench.service.AlertNoticeService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 预警通知服务实现。
 */
@Service
public class AlertNoticeServiceImpl extends ServiceImpl<AlertNoticeMapper, AlertNotice> implements AlertNoticeService {

    @Override
    public IPage<AlertNotice> page(long pageNum, long pageSize, String keyword, Integer status, Integer level) {
        LambdaQueryWrapper<AlertNotice> wrapper = new LambdaQueryWrapper<AlertNotice>()
                .like(StringUtils.hasText(keyword), AlertNotice::getTitle, keyword)
                .eq(status != null, AlertNotice::getStatus, status)
                .eq(level != null, AlertNotice::getLevel, level)
                .orderByAsc(AlertNotice::getStatus)
                .orderByDesc(AlertNotice::getLevel);
        return page(Page.of(pageNum, pageSize), wrapper);
    }

    @Override
    public void handle(Long id) {
        AlertNotice notice = getById(id);
        if (notice == null) {
            throw new BusinessException("预警通知不存在");
        }
        notice.setStatus(1);
        updateById(notice);
    }
}
