package com.ihr.common.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 安全上下文工具：获取当前登录用户（JWT 过滤器写入 SecurityContext 的 username）。
 */
public final class SecurityUtils {

    private SecurityUtils() {
    }

    /** 当前登录用户名，未登录返回 null */
    public static String currentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : null;
    }

    /** 当前登录用户显示名（默认与用户名一致） */
    public static String currentDisplayName() {
        return currentUsername();
    }
}
