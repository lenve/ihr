package com.ihr.module.declare.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ihr.module.declare.entity.VisaApplication;

/**
 * 签证申请服务。
 */
public interface VisaApplicationService extends IService<VisaApplication> {

    /** 分页查询：关键字（申请人模糊）+ 状态 */
    IPage<VisaApplication> page(long pageNum, long pageSize, String keyword, Integer status);

    /** 通过（status=1） */
    void approve(Long id);

    /** 驳回（status=2） */
    void reject(Long id);

    /** 已办理（status=3） */
    void done(Long id);
}
