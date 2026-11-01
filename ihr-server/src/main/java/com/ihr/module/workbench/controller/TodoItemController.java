package com.ihr.module.workbench.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ihr.common.result.Result;
import com.ihr.module.workbench.entity.TodoItem;
import com.ihr.module.workbench.service.TodoItemService;
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
 * 待办通知接口。
 */
@Tag(name = "10-个人工作台-待办通知")
@RestController
@RequestMapping("/api/workbench/todos")
public class TodoItemController {

    private final TodoItemService todoItemService;

    public TodoItemController(TodoItemService todoItemService) {
        this.todoItemService = todoItemService;
    }

    @Operation(summary = "分页查询")
    @GetMapping
    public Result<IPage<TodoItem>> page(@RequestParam(defaultValue = "1") long pageNum,
                                        @RequestParam(defaultValue = "10") long pageSize,
                                        @RequestParam(required = false) String keyword,
                                        @RequestParam(required = false) Integer status) {
        return Result.ok(todoItemService.page(pageNum, pageSize, keyword, status));
    }

    @Operation(summary = "新增")
    @PostMapping
    public Result<Void> create(@RequestBody TodoItem todoItem) {
        todoItem.setId(null);
        todoItemService.save(todoItem);
        return Result.ok();
    }

    @Operation(summary = "修改")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody TodoItem todoItem) {
        todoItem.setId(id);
        todoItemService.updateById(todoItem);
        return Result.ok();
    }

    @Operation(summary = "删除")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        todoItemService.removeById(id);
        return Result.ok();
    }

    @Operation(summary = "标记完成")
    @PutMapping("/{id}/finish")
    public Result<Void> finish(@PathVariable Long id) {
        todoItemService.finish(id);
        return Result.ok();
    }
}
