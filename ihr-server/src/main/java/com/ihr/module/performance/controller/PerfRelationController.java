package com.ihr.module.performance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ihr.common.result.Result;
import com.ihr.module.performance.entity.PerfRelation;
import com.ihr.module.performance.service.PerfRelationService;
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
 * 考核关系接口。
 */
@Tag(name = "绩效考核-考核关系")
@RestController
@RequestMapping("/api/performance/relations")
public class PerfRelationController {

    private final PerfRelationService perfRelationService;

    public PerfRelationController(PerfRelationService perfRelationService) {
        this.perfRelationService = perfRelationService;
    }

    @Operation(summary = "分页查询")
    @GetMapping
    public Result<IPage<PerfRelation>> page(@RequestParam(defaultValue = "1") long pageNum,
                                            @RequestParam(defaultValue = "10") long pageSize,
                                            @RequestParam(required = false) String keyword,
                                            @RequestParam(required = false) String period) {
        return Result.ok(perfRelationService.page(pageNum, pageSize, keyword, period));
    }

    @Operation(summary = "新增")
    @PostMapping
    public Result<Void> create(@RequestBody PerfRelation perfRelation) {
        perfRelation.setId(null);
        perfRelationService.save(perfRelation);
        return Result.ok();
    }

    @Operation(summary = "修改")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody PerfRelation perfRelation) {
        perfRelation.setId(id);
        perfRelationService.updateById(perfRelation);
        return Result.ok();
    }

    @Operation(summary = "删除")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        perfRelationService.removeById(id);
        return Result.ok();
    }
}
