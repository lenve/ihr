package com.ihr.module.talent.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ihr.common.result.Result;
import com.ihr.module.talent.entity.HrPool;
import com.ihr.module.talent.service.HrPoolService;
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
 * 人力资源池接口。
 */
@Tag(name = "23-招聘与人才-人力资源池")
@RestController
@RequestMapping("/api/talent/pools")
public class HrPoolController {

    private final HrPoolService hrPoolService;

    public HrPoolController(HrPoolService hrPoolService) {
        this.hrPoolService = hrPoolService;
    }

    @Operation(summary = "分页查询")
    @GetMapping
    public Result<IPage<HrPool>> page(@RequestParam(defaultValue = "1") long pageNum,
                                      @RequestParam(defaultValue = "10") long pageSize,
                                      @RequestParam(required = false) String keyword,
                                      @RequestParam(required = false) Integer status,
                                      @RequestParam(required = false) Integer poolType) {
        return Result.ok(hrPoolService.page(pageNum, pageSize, keyword, status, poolType));
    }

    @Operation(summary = "新增")
    @PostMapping
    public Result<Void> create(@RequestBody HrPool hrPool) {
        hrPool.setId(null);
        hrPoolService.save(hrPool);
        return Result.ok();
    }

    @Operation(summary = "修改")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody HrPool hrPool) {
        hrPool.setId(id);
        hrPoolService.updateById(hrPool);
        return Result.ok();
    }

    @Operation(summary = "删除")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        hrPoolService.removeById(id);
        return Result.ok();
    }
}
