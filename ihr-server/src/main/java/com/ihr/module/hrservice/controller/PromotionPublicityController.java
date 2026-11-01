package com.ihr.module.hrservice.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ihr.common.result.Result;
import com.ihr.module.hrservice.entity.PromotionPublicity;
import com.ihr.module.hrservice.service.PromotionPublicityService;
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
 * 晋升提拔公示接口。
 */
@Tag(name = "40-人事服务-晋升提拔公示")
@RestController
@RequestMapping("/api/hrservice/promotions")
public class PromotionPublicityController {

    private final PromotionPublicityService promotionPublicityService;

    public PromotionPublicityController(PromotionPublicityService promotionPublicityService) {
        this.promotionPublicityService = promotionPublicityService;
    }

    @Operation(summary = "分页查询")
    @GetMapping
    public Result<IPage<PromotionPublicity>> page(@RequestParam(defaultValue = "1") long pageNum,
                                                  @RequestParam(defaultValue = "10") long pageSize,
                                                  @RequestParam(required = false) String keyword,
                                                  @RequestParam(required = false) Integer status) {
        return Result.ok(promotionPublicityService.page(pageNum, pageSize, keyword, status));
    }

    @Operation(summary = "新增")
    @PostMapping
    public Result<Void> create(@RequestBody PromotionPublicity promotionPublicity) {
        promotionPublicity.setId(null);
        promotionPublicityService.save(promotionPublicity);
        return Result.ok();
    }

    @Operation(summary = "修改")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody PromotionPublicity promotionPublicity) {
        promotionPublicity.setId(id);
        promotionPublicityService.updateById(promotionPublicity);
        return Result.ok();
    }

    @Operation(summary = "删除")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        promotionPublicityService.removeById(id);
        return Result.ok();
    }

    @Operation(summary = "公示通过")
    @PutMapping("/{id}/pass")
    public Result<Void> pass(@PathVariable Long id) {
        promotionPublicityService.pass(id);
        return Result.ok();
    }

    @Operation(summary = "有异议")
    @PutMapping("/{id}/object")
    public Result<Void> object(@PathVariable Long id) {
        promotionPublicityService.markObjection(id);
        return Result.ok();
    }
}
