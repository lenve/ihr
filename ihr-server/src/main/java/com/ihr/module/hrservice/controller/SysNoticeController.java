package com.ihr.module.hrservice.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ihr.common.result.Result;
import com.ihr.module.hrservice.entity.SysNotice;
import com.ihr.module.hrservice.service.SysNoticeService;
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
 * 通知公告接口。
 */
@Tag(name = "40-人事服务-通知公告")
@RestController
@RequestMapping("/api/hrservice/notices")
public class SysNoticeController {

    private final SysNoticeService sysNoticeService;

    public SysNoticeController(SysNoticeService sysNoticeService) {
        this.sysNoticeService = sysNoticeService;
    }

    @Operation(summary = "分页查询")
    @GetMapping
    public Result<IPage<SysNotice>> page(@RequestParam(defaultValue = "1") long pageNum,
                                         @RequestParam(defaultValue = "10") long pageSize,
                                         @RequestParam(required = false) String keyword,
                                         @RequestParam(required = false) Integer noticeType,
                                         @RequestParam(required = false) Integer status) {
        return Result.ok(sysNoticeService.page(pageNum, pageSize, keyword, noticeType, status));
    }

    @Operation(summary = "新增")
    @PostMapping
    public Result<Void> create(@RequestBody SysNotice sysNotice) {
        sysNotice.setId(null);
        sysNoticeService.save(sysNotice);
        return Result.ok();
    }

    @Operation(summary = "修改")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody SysNotice sysNotice) {
        sysNotice.setId(id);
        sysNoticeService.updateById(sysNotice);
        return Result.ok();
    }

    @Operation(summary = "删除")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        sysNoticeService.removeById(id);
        return Result.ok();
    }

    @Operation(summary = "发布")
    @PutMapping("/{id}/publish")
    public Result<Void> publish(@PathVariable Long id) {
        sysNoticeService.publish(id);
        return Result.ok();
    }

    @Operation(summary = "下线")
    @PutMapping("/{id}/offline")
    public Result<Void> offline(@PathVariable Long id) {
        sysNoticeService.offline(id);
        return Result.ok();
    }
}
