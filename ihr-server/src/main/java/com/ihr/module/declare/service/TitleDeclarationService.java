package com.ihr.module.declare.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ihr.module.declare.entity.TitleDeclaration;

/**
 * 职称申报服务。
 */
public interface TitleDeclarationService extends IService<TitleDeclaration> {

    /** 分页查询：关键字（申请人模糊）+ 状态 */
    IPage<TitleDeclaration> page(long pageNum, long pageSize, String keyword, Integer status);

    /** 通过（status=1） */
    void pass(Long id);

    /** 驳回（status=2） */
    void reject(Long id);
}
