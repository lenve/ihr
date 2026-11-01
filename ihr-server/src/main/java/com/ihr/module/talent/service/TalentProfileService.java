package com.ihr.module.talent.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ihr.module.talent.entity.TalentProfile;

/**
 * 人才库服务。
 */
public interface TalentProfileService extends IService<TalentProfile> {

    /** 分页查询：支持关键字（姓名模糊）与状态过滤 */
    IPage<TalentProfile> page(long pageNum, long pageSize, String keyword, Integer status);
}
