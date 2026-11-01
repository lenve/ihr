package com.ihr.module.talent.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ihr.common.result.Result;
import com.ihr.module.talent.entity.TalentDeclaration;
import com.ihr.module.talent.service.TalentDeclarationService;
import com.ihr.module.workbench.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.util.StringUtils;
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
 * 人才申报接口。
 */
@Tag(name = "22-招聘与人才-人才申报")
@RestController
@RequestMapping("/api/talent/declarations")
public class TalentDeclarationController {

    private final TalentDeclarationService talentDeclarationService;
    private final EmployeeService employeeService;

    public TalentDeclarationController(TalentDeclarationService talentDeclarationService,
                                       EmployeeService employeeService) {
        this.talentDeclarationService = talentDeclarationService;
        this.employeeService = employeeService;
    }

    @Operation(summary = "分页查询")
    @GetMapping
    public Result<IPage<TalentDeclaration>> page(@RequestParam(defaultValue = "1") long pageNum,
                                                 @RequestParam(defaultValue = "10") long pageSize,
                                                 @RequestParam(required = false) String keyword,
                                                 @RequestParam(required = false) Integer status) {
        return Result.ok(talentDeclarationService.page(pageNum, pageSize, keyword, status));
    }

    @Operation(summary = "新增", description = "申报人为空时自动填充当前用户")
    @PostMapping
    public Result<Void> create(@RequestBody TalentDeclaration declaration) {
        declaration.setId(null);
        if (!StringUtils.hasText(declaration.getDeclarerName())) {
            declaration.setDeclarerName(employeeService.currentRealName());
        }
        talentDeclarationService.save(declaration);
        return Result.ok();
    }

    @Operation(summary = "修改")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody TalentDeclaration declaration) {
        declaration.setId(id);
        talentDeclarationService.updateById(declaration);
        return Result.ok();
    }

    @Operation(summary = "删除")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        talentDeclarationService.removeById(id);
        return Result.ok();
    }

    @Operation(summary = "审核通过", description = "status=1，审核意见/审核人/审核时间自动填充")
    @PutMapping("/{id}/pass")
    public Result<Void> pass(@PathVariable Long id) {
        talentDeclarationService.pass(id);
        return Result.ok();
    }

    @Operation(summary = "审核驳回", description = "status=2，审核意见/审核人/审核时间自动填充")
    @PutMapping("/{id}/reject")
    public Result<Void> reject(@PathVariable Long id) {
        talentDeclarationService.reject(id);
        return Result.ok();
    }
}
