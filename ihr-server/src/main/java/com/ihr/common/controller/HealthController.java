package com.ihr.common.controller;

import com.ihr.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 健康检查接口（无需登录）。
 */
@Tag(name = "00-健康检查")
@RestController
@RequestMapping("/api/health")
public class HealthController {

    @Value("${spring.application.name:ihr-server}")
    private String applicationName;

    @Operation(summary = "服务健康检查")
    @GetMapping
    public Result<Map<String, Object>> health() {
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("status", "UP");
        info.put("app", applicationName);
        info.put("time", LocalDateTime.now().toString());
        return Result.ok(info);
    }
}
