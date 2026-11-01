package com.ihr.module.workbench.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ihr.module.workbench.entity.Employee;

/**
 * 员工档案服务。
 */
public interface EmployeeService extends IService<Employee> {

    /** 按登录账号查员工档案，可能为 null */
    Employee getByUsername(String username);

    /** 当前登录用户的姓名（无档案时回退为用户名）。业务模块用它匹配 emp_name 等字段。 */
    String currentRealName();
}
