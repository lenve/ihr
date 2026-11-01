package com.ihr.module.hrservice.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ihr.module.hrservice.entity.PromotionPublicity;

/**
 * 晋升提拔公示服务。
 */
public interface PromotionPublicityService extends IService<PromotionPublicity> {

    /** 分页查询：关键字（姓名模糊）+ 状态 */
    IPage<PromotionPublicity> page(long pageNum, long pageSize, String keyword, Integer status);

    /** 公示通过（status=1） */
    void pass(Long id);

    /** 有异议（status=2） */
    void markObjection(Long id);
}
