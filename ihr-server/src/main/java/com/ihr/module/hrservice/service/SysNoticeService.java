package com.ihr.module.hrservice.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ihr.module.hrservice.entity.SysNotice;

/**
 * 通知公告服务。
 */
public interface SysNoticeService extends IService<SysNotice> {

    /** 分页查询：关键字（标题模糊）+ 类型 + 状态 */
    IPage<SysNotice> page(long pageNum, long pageSize, String keyword, Integer noticeType, Integer status);

    /** 发布（status=1，记录发布时间） */
    void publish(Long id);

    /** 下线（status=2） */
    void offline(Long id);
}
