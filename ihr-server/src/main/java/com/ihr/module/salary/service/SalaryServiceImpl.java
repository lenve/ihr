package com.ihr.module.salary.service;

import org.springframework.stereotype.Service;

/**
 * 薪酬模块服务实现骨架。
 */
@Service
public class SalaryServiceImpl implements SalaryService {

    @Override
    public String moduleStatus() {
        return "salary module skeleton ready";
    }
}
