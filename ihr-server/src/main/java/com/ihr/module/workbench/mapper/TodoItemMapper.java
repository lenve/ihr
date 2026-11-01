package com.ihr.module.workbench.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ihr.module.workbench.entity.TodoItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * 待办通知 Mapper。
 */
@Mapper
public interface TodoItemMapper extends BaseMapper<TodoItem> {
}
