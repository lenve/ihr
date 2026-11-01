package com.ihr.module.workbench.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ihr.common.util.SecurityUtils;
import com.ihr.module.workbench.entity.Employee;
import com.ihr.module.workbench.mapper.EmployeeMapper;
import com.ihr.module.workbench.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * 员工档案服务实现。
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    @Override
    public Employee getByUsername(String username) {
        if (username == null || username.isBlank()) {
            return null;
        }
        return lambdaQuery().eq(Employee::getUsername, username).one();
    }

    @Override
    public String currentRealName() {
        String username = SecurityUtils.currentUsername();
        Employee employee = getByUsername(username);
        return employee != null ? employee.getRealName() : username;
    }
}
