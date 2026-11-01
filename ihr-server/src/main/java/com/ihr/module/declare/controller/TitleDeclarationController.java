package com.ihr.module.declare.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ihr.common.result.Result;
import com.ihr.module.declare.entity.TitleDeclaration;
import com.ihr.module.declare.service.TitleDeclarationService;
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
 * 职称申报接口。
 */
@Tag(name = "50-申报与审批-职称申报")
@RestController
@RequestMapping("/api/declare/titles")
public class TitleDeclarationController {

    private final TitleDeclarationService titleDeclarationService;

    public TitleDeclarationController(TitleDeclarationService titleDeclarationService) {
        this.titleDeclarationService = titleDeclarationService;
    }

    @Operation(summary = "分页查询")
    @GetMapping
    public Result<IPage<TitleDeclaration>> page(@RequestParam(defaultValue = "1") long pageNum,
                                                @RequestParam(defaultValue = "10") long pageSize,
                                                @RequestParam(required = false) String keyword,
                                                @RequestParam(required = false) Integer status) {
        return Result.ok(titleDeclarationService.page(pageNum, pageSize, keyword, status));
    }

    @Operation(summary = "新增")
    @PostMapping
    public Result<Void> create(@RequestBody TitleDeclaration titleDeclaration) {
        titleDeclaration.setId(null);
        titleDeclarationService.save(titleDeclaration);
        return Result.ok();
    }

    @Operation(summary = "修改")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody TitleDeclaration titleDeclaration) {
        titleDeclaration.setId(id);
        titleDeclarationService.updateById(titleDeclaration);
        return Result.ok();
    }

    @Operation(summary = "删除")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        titleDeclarationService.removeById(id);
        return Result.ok();
    }

    @Operation(summary = "通过")
    @PutMapping("/{id}/pass")
    public Result<Void> pass(@PathVariable Long id) {
        titleDeclarationService.pass(id);
        return Result.ok();
    }

    @Operation(summary = "驳回")
    @PutMapping("/{id}/reject")
    public Result<Void> reject(@PathVariable Long id) {
        titleDeclarationService.reject(id);
        return Result.ok();
    }
}
