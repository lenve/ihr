package com.ihr.module.talent.controller;

import com.ihr.common.result.Result;
import com.ihr.module.talent.service.TalentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 人才模块骨架（人才画像、梯队、继任计划等，与招聘模块解耦）。
 */
@Tag(name = "人才模块")
@RestController
@RequestMapping("/api/talent")
public class TalentController {

    private final TalentService talentService;

    public TalentController(TalentService talentService) {
        this.talentService = talentService;
    }

    @Operation(summary = "模块探活", description = "框架占位，后续在此补充各业务接口")
    @GetMapping("/ping")
    public Result<String> ping() {
        return Result.ok(talentService.moduleStatus());
    }
}
