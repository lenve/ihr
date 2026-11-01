package com.ihr.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JWT 配置项（对应 application.yml 中 jwt.* 前缀）。
 */
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    /** HS256 签名密钥，至少 32 字节 */
    private String secret = "ihr-jwt-secret-key-please-change-in-production-0123456789";

    /** token 有效期（毫秒），默认 8 小时 */
    private long expire = 28800000L;

    /** 请求头名称 */
    private String header = "Authorization";

    /** token 前缀 */
    private String prefix = "Bearer ";

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
