package com.ihr.module.talent.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ihr.common.result.Result;
import com.ihr.module.talent.entity.TalentProfile;
import com.ihr.module.talent.service.TalentProfileService;
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
 * 人才库接口。
 */
@Tag(name = "21-招聘与人才-人才库")
@RestController
@RequestMapping("/api/talent/profiles")
public class TalentProfileController {

    private final TalentProfileService talentProfileService;

    public TalentProfileController(TalentProfileService talentProfileService) {
        this.talentProfileService = talentProfileService;
    }

    @Operation(summary = "分页查询")
    @GetMapping
    public Result<IPage<TalentProfile>> page(@RequestParam(defaultValue = "1") long pageNum,
                                             @RequestParam(defaultValue = "10") long pageSize,
                                             @RequestParam(required = false) String keyword,
                                             @RequestParam(required = false) Integer status) {
        return Result.ok(talentProfileService.page(pageNum, pageSize, keyword, status));
    }

    @Operation(summary = "新增")
    @PostMapping
    public Result<Void> create(@RequestBody TalentProfile talentProfile) {
        talentProfile.setId(null);
        talentProfileService.save(talentProfile);
        return Result.ok();
    }

    @Operation(summary = "修改")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody TalentProfile talentProfile) {
        talentProfile.setId(id);
        talentProfileService.updateById(talentProfile);
        return Result.ok();
    }

    @Operation(summary = "删除")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        talentProfileService.removeById(id);
        return Result.ok();
    }
}
