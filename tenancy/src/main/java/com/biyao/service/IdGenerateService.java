package com.biyao.service;

import com.biyao.config.properties.TenantConfigProperties;
import com.biyao.error.CommonException;
import com.biyao.error.ErrorCode;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.biyao.config.properties.TenantConfigProperties.DEFAULT_REQUEST;
import static com.biyao.constants.TenantRemote.TenancyMethods.ID_GENERATE;
import static org.springframework.http.HttpMethod.GET;

/**
 * id生成Service
 *
 * @Author tanglh
 * @Date 2019/1/3 20:03
 */
@Service
public class IdGenerateService {

    private static final ParameterizedTypeReference<Long> RESPONSE = new ParameterizedTypeReference<Long>() {
    };


    private final String BASE_URL;
    private final RestTemplate REST_TEMPLATE;

    public IdGenerateService(TenantConfigProperties tenantConfigProperties) {
        this.BASE_URL = tenantConfigProperties.getTenancyBaseUrl();
        this.REST_TEMPLATE = tenantConfigProperties.getTenancyRestTemplate();
    }


    /**
     * 生成id
     *
     * @return
     */
    public Long generateId() {
        ResponseEntity<Long> response = REST_TEMPLATE.exchange(getRequestUri(ID_GENERATE), GET, DEFAULT_REQUEST, RESPONSE);
        if (response.getStatusCode().value() < HttpStatus.OK.value() || response.getStatusCode().value() > HttpStatus.MULTIPLE_CHOICES.value()) {
            throw new CommonException(ErrorCode.proc_failed);
        }
        return response.getBody();
    }

    /**
     * 获取请求url
     *
     * @param method
     * @param replace
     * @return
     */
    private String getRequestUri(String method, Object... replace) {
        if (null == replace) {
            return BASE_URL + method;
        }
        return BASE_URL + String.format(method, replace);
    }
}