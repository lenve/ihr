package com.ihr.module.workbench.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ihr.common.exception.BusinessException;
import com.ihr.module.workbench.entity.WorkTask;
import com.ihr.module.workbench.mapper.WorkTaskMapper;
import com.ihr.module.workbench.service.WorkTaskService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * 工作任务服务实现。
 */
@Service
public class WorkTaskServiceImpl extends ServiceImpl<WorkTaskMapper, WorkTask> implements WorkTaskService {

    @Override
    public IPage<WorkTask> page(long pageNum, long pageSize, String keyword, Integer status) {
        LambdaQueryWrapper<WorkTask> wrapper = new LambdaQueryWrapper<WorkTask>()
                .like(StringUtils.hasText(keyword), WorkTask::getTitle, keyword)
                .eq(status != null, WorkTask::getStatus, status)
                .orderByAsc(WorkTask::getStatus)
                .orderByAsc(WorkTask::getDeadline);
        return page(Page.of(pageNum, pageSize), wrapper);
    }

    @Override
    public void complete(Long id) {
        WorkTask task = getById(id);
        if (task == null) {
            throw new BusinessException("任务不存在");
        }
        task.setStatus(1);
        task.setProgress(100);
        task.setFinishTime(LocalDateTime.now());
        updateById(task);
    }
}
