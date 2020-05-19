package com.biyao.security.client;

import com.biyao.error.CloudException;
import com.biyao.error.ErrorCode;
import com.biyao.security.config.SecurityProperties;
import com.biyao.security.constant.SecurityConstants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * 功能描述 oauth2 token相关接口基础实现
 *
 * @author hxs
 * @date 2020/5/19
 */
public abstract class OAuth2TokenEndpointClientAdapter implements OAuth2TokenEndpointClient {
    private final Logger log = LoggerFactory.getLogger(OAuth2TokenEndpointClientAdapter.class);
    protected RestTemplate restTemplate;
    protected SecurityProperties securityProperties;

    /**
     * 属性设置
     */
    protected void setProperties(RestTemplate restTemplate, SecurityProperties securityProperties) {
        this.restTemplate = restTemplate;
        this.securityProperties = securityProperties;
    }

    /**
     * 密码授权
     */
    @Override
    public OAuth2AccessToken sendPasswordGrant(String username, String password, String endpoint, MultiValueMap<String, String> headerMap) {
        HttpHeaders reqHeaders = new HttpHeaders();
        reqHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> formParams = new LinkedMultiValueMap<>();
        formParams.set("username", username);
        formParams.set("password", password);
        formParams.set("grant_type", SecurityConstants.Security.GrantType.password);
        formParams.set("endpoint", endpoint);
        formParams.addAll(headerMap);
        log.debug("login form params: {}", formParams);
        addAuthentication(reqHeaders, formParams);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(formParams, reqHeaders);
        log.debug("contacting OAuth2 token endpoint to login user: {}", username);
        ResponseEntity<OAuth2AccessToken>
                responseEntity = restTemplate.postForEntity(getTokenEndpoint(), entity, OAuth2AccessToken.class);
        OAuth2AccessToken accessToken = responseEntity.getBody();
        log.info("sendPasswordGrant result: {}", accessToken);
        return accessToken;
    }

    /**
     * 短信验证码授权
     */
    @Override
    public OAuth2AccessToken sendVerifyCodeGrant(String phoneNum, String verifyCode, String endpoint, MultiValueMap<String, String> headerMap) {
        HttpHeaders reqHeaders = new HttpHeaders();
        reqHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> formParams = new LinkedMultiValueMap<>();
        formParams.set("phone_num", phoneNum);
        formParams.set("verify_code", verifyCode);
        formParams.set("grant_type", SecurityConstants.Security.GrantType.verifyCode);
        formParams.set("endpoint", endpoint);
        formParams.addAll(headerMap);
        log.debug("login form params: {}", formParams);
        addAuthentication(reqHeaders, formParams);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(formParams, reqHeaders);
        log.debug("contacting OAuth2 token endpoint to login user: {}", phoneNum);
        ResponseEntity<OAuth2AccessToken>
                responseEntity = restTemplate.postForEntity(getTokenEndpoint(), entity, OAuth2AccessToken.class);
        OAuth2AccessToken accessToken = responseEntity.getBody();
        log.info("sendVerifyCodeGrant result: {}", accessToken);
        return accessToken;
    }

    /**
     * 刷新token
     */
    @Override
    public OAuth2AccessToken sendRefreshGrant(String refreshTokenValue) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "refresh_token");
        params.add("refresh_token", refreshTokenValue);
        HttpHeaders headers = new HttpHeaders();
        addAuthentication(headers, params);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
        log.debug("contacting OAuth2 token endpoint to refresh OAuth2 tokens");
        ResponseEntity<OAuth2AccessToken> responseEntity = restTemplate.postForEntity(getTokenEndpoint(), entity,
                OAuth2AccessToken.class);
        OAuth2AccessToken accessToken = responseEntity.getBody();
        log.info("sendRefreshGrant result: {}", accessToken);
        return accessToken;
    }

    /**
     * 校验token
     */
    @Override
    public Map<String, Object> checkAccessToken(String accessTokenValue) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("token", accessTokenValue);
        HttpHeaders headers = new HttpHeaders();
        addAuthentication(headers, params);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
        log.debug("contacting OAuth2 token endpoint to check OAuth2 tokens");
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(getCheckTokenEndpoint(), entity, Map.class);
        @SuppressWarnings("unchecked")
        Map<String, Object> result = responseEntity.getBody();
        log.info("checkAccessToken result: {}", result);
        return result;
    }

    @Override
    public void removeToken(String accessTokenValue) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("token", accessTokenValue);
        HttpHeaders headers = new HttpHeaders();
        addAuthentication(headers, params);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
        log.debug("contacting OAuth2 token endpoint to remove OAuth2 tokens");
        restTemplate.exchange(getRemoveTokenEndpoint(), HttpMethod.PUT, entity, Void.class);
    }

    /**
     * 添加认证头部
     */
    protected abstract void addAuthentication(HttpHeaders reqHeaders, MultiValueMap<String, String> formParams);

    /**
     * 获取client密钥
     */
    protected String getClientSecret() {
        String clientSecret = securityProperties.getSecurity().getClientAuthorization().getClientSecret();
        if (clientSecret == null) {
            throw CloudException.build(ErrorCode.error, "没有配置client密钥");
        }
        return clientSecret;
    }

    /**
     * 获取client ID
     */
    protected String getClientId() {
        String clientId = securityProperties.getSecurity().getClientAuthorization().getClientId();
        if (clientId == null) {
            throw CloudException.build(ErrorCode.error, "没有配置clientID");
        }
        return clientId;
    }

    /**
     * 获取token入口
     */
    protected String getTokenEndpoint() {
        String uaaUrl = securityProperties.getSecurity().getClientAuthorization().getUaaUri();
        String tokenEndpointUrl = securityProperties.getSecurity().getClientAuthorization().getAccessTokenUri();
        if (StringUtils.isBlank(uaaUrl) || StringUtils.isBlank(tokenEndpointUrl)) {
            throw CloudException.build(ErrorCode.error, "没有配置获取token入口");
        }
        return uaaUrl + tokenEndpointUrl;
    }

    /**
     * 获取校验token入口
     */
    protected String getCheckTokenEndpoint() {
        String uaaUrl = securityProperties.getSecurity().getClientAuthorization().getUaaUri();
        String checkTokenEndpointUrl = securityProperties.getSecurity().getClientAuthorization().getCheckTokenUri();
        if (StringUtils.isBlank(uaaUrl) || StringUtils.isBlank(checkTokenEndpointUrl)) {
            throw CloudException.build(ErrorCode.error, "没有配置校验token入口");
        }
        return uaaUrl + checkTokenEndpointUrl;
    }

    /**
     * 获取撤销token入口
     */
    protected String getRemoveTokenEndpoint() {
        String uaaUrl = securityProperties.getSecurity().getClientAuthorization().getUaaUri();
        String removeTokenEndpointUrl = securityProperties.getSecurity().getClientAuthorization().getRemoveTokenUri();
        if (StringUtils.isBlank(uaaUrl) || StringUtils.isBlank(removeTokenEndpointUrl)) {
            throw CloudException.build(ErrorCode.error, "没有配置撤销token入口");
        }
        return uaaUrl + removeTokenEndpointUrl;
    }

}
