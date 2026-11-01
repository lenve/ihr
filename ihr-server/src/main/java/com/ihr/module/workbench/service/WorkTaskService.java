package com.ihr.module.workbench.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ihr.module.workbench.entity.WorkTask;

/**
 * 工作任务服务。
 */
public interface WorkTaskService extends IService<WorkTask> {

    /** 分页查询：支持关键字（标题模糊）与状态过滤 */
    IPage<WorkTask> page(long pageNum, long pageSize, String keyword, Integer status);

    /** 标记完成：status=1、progress=100、finishTime=now */
    void complete(Long id);
}
