package com.ihr.module.workbench.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ihr.module.workbench.entity.WorkTask;
import org.apache.ibatis.annotations.Mapper;

/**
 * 工作任务 Mapper。
 */
@Mapper
public interface WorkTaskMapper extends BaseMapper<WorkTask> {
}
