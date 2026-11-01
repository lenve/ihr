package com.ihr.module.system.service;

import com.ihr.common.exception.BusinessException;
import com.ihr.common.result.ResultCode;
import com.ihr.module.system.dto.LoginDTO;
import com.ihr.module.system.vo.LoginVO;
import com.ihr.module.system.vo.UserInfoVO;
import com.ihr.security.JwtTokenProvider;
import org.springframework.stereotype.Service;

/**
 * 认证服务实现：假数据登录 + JWT 签发。
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final JwtTokenProvider tokenProvider;
    private final FakeUserRepository fakeUserRepository = new FakeUserRepository();

    public AuthServiceImpl(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public LoginVO login(LoginDTO dto) {
        if (!fakeUserRepository.checkPassword(dto.username(), dto.password())) {
            throw new BusinessException(ResultCode.USERNAME_OR_PASSWORD_ERROR);
        }
        UserInfoVO user = fakeUserRepository.findByUsername(dto.username()).orElseThrow(
                () -> new BusinessException(ResultCode.USERNAME_OR_PASSWORD_ERROR));
        String token = tokenProvider.createToken(user.username(), user.roles());
        return new LoginVO(token, tokenProvider.getExpireMillis(), user);
    }

    @Override
    public UserInfoVO getUserInfo(String username) {
        return fakeUserRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(ResultCode.UNAUTHORIZED));
    }
}
