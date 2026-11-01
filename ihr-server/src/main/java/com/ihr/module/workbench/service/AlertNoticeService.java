package com.ihr.module.workbench.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ihr.module.workbench.entity.AlertNotice;

/**
 * 预警通知服务。
 */
public interface AlertNoticeService extends IService<AlertNotice> {

    /** 分页查询：支持关键字（标题模糊）、状态与级别过滤 */
    IPage<AlertNotice> page(long pageNum, long pageSize, String keyword, Integer status, Integer level);

    /** 标记已处理 */
    void handle(Long id);
}
