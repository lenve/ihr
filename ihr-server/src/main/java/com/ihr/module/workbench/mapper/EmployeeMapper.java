package com.ihr.module.workbench.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ihr.module.workbench.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * 员工档案 Mapper。
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
