package com.ihr.module.hrservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ihr.common.exception.BusinessException;
import com.ihr.module.hrservice.entity.SysNotice;
import com.ihr.module.hrservice.mapper.SysNoticeMapper;
import com.ihr.module.hrservice.service.SysNoticeService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * 通知公告服务实现。
 */
@Service
public class SysNoticeServiceImpl extends ServiceImpl<SysNoticeMapper, SysNotice> implements SysNoticeService {

    @Override
    public IPage<SysNotice> page(long pageNum, long pageSize, String keyword, Integer noticeType, Integer status) {
        LambdaQueryWrapper<SysNotice> wrapper = new LambdaQueryWrapper<SysNotice>()
                .like(StringUtils.hasText(keyword), SysNotice::getTitle, keyword)
                .eq(noticeType != null, SysNotice::getNoticeType, noticeType)
                .eq(status != null, SysNotice::getStatus, status)
                .orderByDesc(SysNotice::getPublishTime)
                .orderByDesc(SysNotice::getId);
        return page(Page.of(pageNum, pageSize), wrapper);
    }

    @Override
    public void publish(Long id) {
        SysNotice notice = getById(id);
        if (notice == null) {
            throw new BusinessException("通知公告不存在");
        }
        notice.setStatus(1);
        notice.setPublishTime(LocalDateTime.now());
        updateById(notice);
    }

    @Override
    public void offline(Long id) {
        SysNotice notice = getById(id);
        if (notice == null) {
            throw new BusinessException("通知公告不存在");
        }
        notice.setStatus(2);
        updateById(notice);
    }
}
