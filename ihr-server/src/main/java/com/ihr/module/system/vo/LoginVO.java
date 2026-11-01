package com.ihr.module.system.vo;

import java.util.List;

/**
 * 登录成功返回信息。
 */
public record LoginVO(
        String token,
        long expiresIn,
        UserInfoVO user
) {
}
