package com.ihr.module.declare.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ihr.module.declare.entity.TitleRecord;

/**
 * 职称记录服务。
 */
public interface TitleRecordService extends IService<TitleRecord> {

    /** 分页查询：关键字（姓名模糊）+ 状态 */
    IPage<TitleRecord> page(long pageNum, long pageSize, String keyword, Integer status);
}
