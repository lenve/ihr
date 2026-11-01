package com.ihr.module.hrservice.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ihr.common.result.Result;
import com.ihr.module.hrservice.entity.CmsArticle;
import com.ihr.module.hrservice.service.CmsArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 内容文章接口（政策解读/办事指南/出入境指南）。
 */
@Tag(name = "40-人事服务-内容文章")
@RestController
@RequestMapping("/api/hrservice/articles")
public class CmsArticleController {

    private final CmsArticleService cmsArticleService;

    public CmsArticleController(CmsArticleService cmsArticleService) {
        this.cmsArticleService = cmsArticleService;
    }

    @Operation(summary = "分页查询")
    @GetMapping
    public Result<IPage<CmsArticle>> page(@RequestParam(defaultValue = "1") long pageNum,
                                          @RequestParam(defaultValue = "10") long pageSize,
                                          @RequestParam(required = false) String keyword,
                                          @RequestParam(required = false) String category,
                                          @RequestParam(required = false) Integer status) {
        return Result.ok(cmsArticleService.page(pageNum, pageSize, keyword, category, status));
    }

    @Operation(summary = "新增")
    @PostMapping
    public Result<Void> create(@RequestBody CmsArticle cmsArticle) {
        cmsArticle.setId(null);
        if (cmsArticle.getViewCount() == null) {
            cmsArticle.setViewCount(0);
        }
        cmsArticleService.save(cmsArticle);
        return Result.ok();
    }

    @Operation(summary = "修改")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody CmsArticle cmsArticle) {
        cmsArticle.setId(id);
        cmsArticleService.updateById(cmsArticle);
        return Result.ok();
    }

    @Operation(summary = "删除")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        cmsArticleService.removeById(id);
        return Result.ok();
    }
}
