package com.ihr.module.hrservice.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ihr.module.hrservice.entity.CmsArticle;

/**
 * 内容文章服务（政策解读/办事指南/出入境指南）。
 */
public interface CmsArticleService extends IService<CmsArticle> {

    /** 分页查询：关键字（标题模糊）+ 分类 + 状态 */
    IPage<CmsArticle> page(long pageNum, long pageSize, String keyword, String category, Integer status);
}
