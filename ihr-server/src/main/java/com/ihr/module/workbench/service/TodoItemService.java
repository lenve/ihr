package com.ihr.module.workbench.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ihr.module.workbench.entity.TodoItem;

/**
 * 待办通知服务。
 */
public interface TodoItemService extends IService<TodoItem> {

    /** 分页查询：支持关键字（标题模糊）与状态过滤 */
    IPage<TodoItem> page(long pageNum, long pageSize, String keyword, Integer status);

    /** 标记完成 */
    void finish(Long id);
}
