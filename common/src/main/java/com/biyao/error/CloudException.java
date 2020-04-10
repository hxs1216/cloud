package com.biyao.error;

import org.apache.commons.lang3.StringUtils;

/**
 * 业务公共异常
 *
 * @description:
 * @author: hxs
 * @create: 2020/4/10
 */
public class CloudException extends CommonException {

    public CloudException(ExceptionData data) {
        super(data);
    }

    public static CloudException build(ErrorCode code) {
        ExceptionData data = new ExceptionData(code.getCode(), code.getMessage());
        return new CloudException(data);
    }

    public static CloudException build(ErrorCode code, String extInfo) {
        ExceptionData data;
        if (StringUtils.isBlank(extInfo)) {
            data = new ExceptionData(code.getCode(), code.getMessage());
        } else {
            data = new ExceptionData(code.getCode(),
                    StringUtils.isBlank(code.getMessage()) ? extInfo : code.getMessage() + ":" + extInfo);
        }
        return new CloudException(data);
    }
}
