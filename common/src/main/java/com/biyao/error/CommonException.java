package com.biyao.error;

import org.apache.commons.lang3.StringUtils;

/**
 * 公共异常
 *
 * @description:
 * @author: hxs
 * @create: 2020/4/10
 */
public class CommonException extends RuntimeException {
    private ExceptionData data;

    public CommonException(ExceptionData data) {
        super(data.getMessage());
        this.data = data;
    }

    public CommonException(ErrorCode code) {
        super(code.getMessage());
        this.data = new ExceptionData(code.getCode(), code.getMessage());
    }

    public CommonException(ErrorCode code, String extInfo) {
        super(code.getMessage() + extInfo);
        if (StringUtils.isBlank(extInfo)) {
            this.data = new ExceptionData(code.getCode(), code.getMessage());
        } else {
            this.data = new ExceptionData(code.getCode(), StringUtils.isBlank(code.getMessage()) ? extInfo : code.getMessage() + ":" + extInfo);
        }
    }

    @Override
    public CommonException initCause(Throwable throwable){
        super.initCause(throwable);
        return this;
    }

}