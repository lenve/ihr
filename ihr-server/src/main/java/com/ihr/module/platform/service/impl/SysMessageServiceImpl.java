package com.ihr.module.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ihr.common.exception.BusinessException;
import com.ihr.module.platform.dto.SendMessageRequest;
import com.ihr.module.platform.entity.SysMessage;
import com.ihr.module.platform.mapper.SysMessageMapper;
import com.ihr.module.platform.service.SysMessageService;
import com.ihr.module.workbench.service.EmployeeService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 站内消息服务实现。
 */
@Service
public class SysMessageServiceImpl extends ServiceImpl<SysMessageMapper, SysMessage> implements SysMessageService {

    private final EmployeeService employeeService;

    public SysMessageServiceImpl(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public IPage<SysMessage> page(long pageNum, long pageSize, String box) {
        String currentName = employeeService.currentRealName();
        LambdaQueryWrapper<SysMessage> wrapper = new LambdaQueryWrapper<>();
        if ("sent".equals(box)) {
            wrapper.eq(SysMessage::getSenderName, currentName);
        } else {
            wrapper.eq(SysMessage::getReceiverName, currentName);
        }
        wrapper.orderByDesc(SysMessage::getCreateTime)
                .orderByDesc(SysMessage::getId);
        return page(Page.of(pageNum, pageSize), wrapper);
    }

    @Override
    public void send(SendMessageRequest request) {
        if (request == null || !StringUtils.hasText(request.receiverName()) || !StringUtils.hasText(request.title())) {
            throw new BusinessException("收件人与标题不能为空");
        }
        SysMessage message = new SysMessage();
        message.setSenderName(employeeService.currentRealName());
        message.setReceiverName(request.receiverName().trim());
        message.setTitle(request.title().trim());
        message.setContent(request.content());
        message.setReadFlag(0);
        save(message);
    }

    @Override
    public void markRead(Long id) {
        SysMessage message = getById(id);
        if (message == null) {
            throw new BusinessException("消息不存在");
        }
        message.setReadFlag(1);
        updateById(message);
    }

    @Override
    public long unreadCount() {
        String currentName = employeeService.currentRealName();
        return count(new LambdaQueryWrapper<SysMessage>()
                .eq(SysMessage::getReceiverName, currentName)
                .eq(SysMessage::getReadFlag, 0));
    }
}
