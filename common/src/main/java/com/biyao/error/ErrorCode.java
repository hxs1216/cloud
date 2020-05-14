package com.biyao.error;

/**
 * 业务异常
 */
public enum ErrorCode {
    error(-1, ""),
    system_error(0, "系统错误"),
    common_error(1, "业务异常"),
    invalid_params(2, "非法参数"),
    proc_failed(3, "处理失败"),
    current_tenant_code_not_found(4,"当前租户信息不存在"),
    cannot_get_data_source(5,"无法获取数据源"),
    data_source_has_init(6,"数据源已经初始化"),
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
