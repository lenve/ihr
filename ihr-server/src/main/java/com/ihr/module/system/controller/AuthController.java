package com.ihr.module.system.controller;

import com.ihr.common.result.Result;
import com.ihr.module.system.dto.LoginDTO;
import com.ihr.module.system.service.AuthService;
import com.ihr.module.system.vo.LoginVO;
import com.ihr.module.system.vo.UserInfoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 认证接口。
 * <p>框架阶段为假数据登录（admin/123456、zhangsan/123456、lisi/123456）。</p>
 */
@Tag(name = "01-认证", description = "登录、当前用户信息")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "登录", description = "假数据校验并签发 JWT")
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginDTO dto) {
        return Result.ok(authService.login(dto));
    }

    @Operation(summary = "当前登录用户信息")
    @GetMapping("/me")
    public Result<UserInfoVO> me(Authentication authentication) {
        String username = authentication != null ? authentication.getName() : null;
        return Result.ok(authService.getUserInfo(username));
    }

    @Operation(summary = "登出", description = "JWT 无状态方案：客户端删除 token 即可")
    @PostMapping("/logout")
    public Result<Void> logout() {
        return Result.ok();
    }
}
