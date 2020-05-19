package com.biyao.security.client;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.util.MultiValueMap;

import java.util.Map;

/**
 * 功能描述 oauth2 token相关接口
 *
 * @author hxs
 * @date 2020/5/19
 */
public interface OAuth2TokenEndpointClient {

    /**
     * 密码授权
     */
    OAuth2AccessToken sendPasswordGrant(String username, String password, String endpoint, MultiValueMap<String, String> headerMap);

    /**
     * 手机验证码授权
     */
    OAuth2AccessToken sendVerifyCodeGrant(String phoneNum, String verifyCode, String endpoint, MultiValueMap<String, String> headerMap);

    /**
     * 刷新token授权
     */
    OAuth2AccessToken sendRefreshGrant(String refreshTokenValue);

    /**
     * 校验token
     */
    Map<String, Object> checkAccessToken(String accessTokenValue);

    /**
     * 取消token
     */
    void removeToken(String accessTokenValue);
}
