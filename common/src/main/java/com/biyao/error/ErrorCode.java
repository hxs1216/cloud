package com.biyao.error;

/**
 * 业务异常
 */
public enum ErrorCode {
    error(-1, ""),
    system_error(0, "系统错误"),
    common_error(5, "业务异常"),
    invalid_params(1, "非法参数"),
    proc_failed(2, "处理失败"),
    data_not_found(3, "数据不存在"),
    data_empty(4, "数据为空"),
    base_data_miss(7, "基础数据缺失"),
    unauthorized(8, "未授权"),
    ;

    private static final int offset = 10000;
    private int code;
    private String message;

    ErrorCode(int code, String message) {
        this.code = code + offset;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
