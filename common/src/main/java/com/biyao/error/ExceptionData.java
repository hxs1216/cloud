package com.biyao.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 异常数据
 *
 * @description: 异常数据
 * @author: hxs
 * @create: 2020/4/10
 */
@Data
@AllArgsConstructor
@Builder
public class ExceptionData implements Serializable {
    private int errorCode;
    private String message;
}