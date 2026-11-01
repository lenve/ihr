package com.ihr.module.platform.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ihr.common.result.Result;
import com.ihr.module.platform.dto.SendMessageRequest;
import com.ihr.module.platform.entity.SysMessage;
import com.ihr.module.platform.service.SysMessageService;
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
 * 信息收发平台接口（站内消息）。
 */
@Tag(name = "60-平台功能-信息收发")
@RestController
@RequestMapping("/api/platform/messages")
public class SysMessageController {

    private final SysMessageService sysMessageService;

    public SysMessageController(SysMessageService sysMessageService) {
        this.sysMessageService = sysMessageService;
    }

    @Operation(summary = "分页查询（box=inbox 收件箱 / sent 已发送）")
    @GetMapping
    public Result<IPage<SysMessage>> page(@RequestParam(defaultValue = "1") long pageNum,
                                          @RequestParam(defaultValue = "10") long pageSize,
                                          @RequestParam(defaultValue = "inbox") String box) {
        return Result.ok(sysMessageService.page(pageNum, pageSize, box));
    }

    @Operation(summary = "发送消息（发件人为当前登录用户）")
    @PostMapping("/send")
    public Result<Void> send(@RequestBody SendMessageRequest request) {
        sysMessageService.send(request);
        return Result.ok();
    }

    @Operation(summary = "标记已读")
    @PutMapping("/{id}/read")
    public Result<Void> read(@PathVariable Long id) {
        sysMessageService.markRead(id);
        return Result.ok();
    }

    @Operation(summary = "删除")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        sysMessageService.removeById(id);
        return Result.ok();
    }

    @Operation(summary = "当前用户未读数")
    @GetMapping("/unread-count")
    public Result<Long> unreadCount() {
        return Result.ok(sysMessageService.unreadCount());
    }
}
