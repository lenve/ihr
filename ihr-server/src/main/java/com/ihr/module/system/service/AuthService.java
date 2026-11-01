package com.ihr.module.system.service;

import com.ihr.module.system.dto.LoginDTO;
import com.ihr.module.system.vo.LoginVO;
import com.ihr.module.system.vo.UserInfoVO;

/**
 * 认证服务。
 * <p>框架阶段使用假数据校验（见 {@link FakeUserRepository}），
 * 接入真实业务后改为查询 sys_user 表 + BCrypt 密码比对。</p>
 */
public interface AuthService {

    /** 登录校验并签发 token */
    LoginVO login(LoginDTO dto);

    /** 根据用户名获取用户信息（供 /me 接口） */
    UserInfoVO getUserInfo(String username);
}
