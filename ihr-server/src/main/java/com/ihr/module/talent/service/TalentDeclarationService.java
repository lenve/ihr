package com.ihr.module.talent.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ihr.module.talent.entity.TalentDeclaration;

/**
 * 人才申报服务。
 */
public interface TalentDeclarationService extends IService<TalentDeclaration> {

    /** 分页查询：支持关键字（被申报人模糊）与状态过滤 */
    IPage<TalentDeclaration> page(long pageNum, long pageSize, String keyword, Integer status);

    /** 审核通过：status=1，审核意见/审核人/审核时间自动填充 */
    void pass(Long id);

    /** 审核驳回：status=2，审核意见/审核人/审核时间自动填充 */
    void reject(Long id);
}
