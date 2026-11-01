package com.ihr;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * iHR 智慧人力资源管理系统后端入口。
 * <p>框架阶段：仅提供登录/健康检查等基础能力与各业务模块空骨架。</p>
 */
@SpringBootApplication
@MapperScan("com.ihr.**.mapper")
public class IhrApplication {

    public static void main(String[] args) {
        SpringApplication.run(IhrApplication.class, args);
    }
}
