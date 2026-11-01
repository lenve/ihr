package com.ihr.module.workbench.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ihr.common.exception.BusinessException;
import com.ihr.module.workbench.entity.TodoItem;
import com.ihr.module.workbench.mapper.TodoItemMapper;
import com.ihr.module.workbench.service.TodoItemService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * 待办通知服务实现。
 */
@Service
public class TodoItemServiceImpl extends ServiceImpl<TodoItemMapper, TodoItem> implements TodoItemService {

    @Override
    public IPage<TodoItem> page(long pageNum, long pageSize, String keyword, Integer status) {
        LambdaQueryWrapper<TodoItem> wrapper = new LambdaQueryWrapper<TodoItem>()
                .like(StringUtils.hasText(keyword), TodoItem::getTitle, keyword)
                .eq(status != null, TodoItem::getStatus, status)
                .orderByAsc(TodoItem::getStatus)
                .orderByAsc(TodoItem::getDueTime);
        return page(Page.of(pageNum, pageSize), wrapper);
    }

    @Override
    public void finish(Long id) {
        TodoItem item = getById(id);
        if (item == null) {
            throw new BusinessException("待办不存在");
        }
        item.setStatus(1);
        item.setFinishTime(LocalDateTime.now());
        updateById(item);
    }
}
