package com.ihr.module.hrservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ihr.module.hrservice.entity.CmsArticle;
import com.ihr.module.hrservice.mapper.CmsArticleMapper;
import com.ihr.module.hrservice.service.CmsArticleService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 内容文章服务实现。
 */
@Service
public class CmsArticleServiceImpl extends ServiceImpl<CmsArticleMapper, CmsArticle> implements CmsArticleService {

    @Override
    public IPage<CmsArticle> page(long pageNum, long pageSize, String keyword, String category, Integer status) {
        LambdaQueryWrapper<CmsArticle> wrapper = new LambdaQueryWrapper<CmsArticle>()
                .like(StringUtils.hasText(keyword), CmsArticle::getTitle, keyword)
                .eq(StringUtils.hasText(category), CmsArticle::getCategory, category)
                .eq(status != null, CmsArticle::getStatus, status)
                .orderByDesc(CmsArticle::getPublishTime)
                .orderByDesc(CmsArticle::getId);
        return page(Page.of(pageNum, pageSize), wrapper);
    }
}
