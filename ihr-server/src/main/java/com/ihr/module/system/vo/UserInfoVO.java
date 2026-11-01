package com.ihr.module.system.vo;

import java.util.List;

/**
 * 当前登录用户信息。
 */
public record UserInfoVO(
        Long id,
        String username,
        String nickname,
        String email,
        String phone,
        List<String> roles
) {
}
