package com.biyao.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Title: 通用返回结果
 * Description: 通用返回结果
 * Copyright: Copyright (c) 2019
 * Company: TaoQiCar Co., Ltd.
 * Create Time: 2020/3/19
 *
 * @author hxs
 * @email huangxiaosheng@xxfqc.com
 * Update Time:
 * Updater:
 * Update Comments:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResult<T> {
    private Integer code;
    private String message;
    private T data;

    public CommonResult(Integer code, String message) {
        this(code, message, null);
    }
} 