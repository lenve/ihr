package com.ihr.module.platform.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ihr.module.platform.dto.SendMessageRequest;
import com.ihr.module.platform.entity.SysMessage;

/**
 * 站内消息服务。
 */
public interface SysMessageService extends IService<SysMessage> {

    /** 分页查询：box=inbox 收件箱（receiver=当前用户）/ sent 已发送（sender=当前用户），按创建时间倒序 */
    IPage<SysMessage> page(long pageNum, long pageSize, String box);

    /** 发送消息（发件人为当前用户） */
    void send(SendMessageRequest request);

    /** 标记已读（readFlag=1） */
    void markRead(Long id);

    /** 当前用户未读消息数 */
    long unreadCount();
}
