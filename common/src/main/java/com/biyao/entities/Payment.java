package com.biyao.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Title: 订单实体类
 * Description: 订单实体类
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
@AllArgsConstructor
@NoArgsConstructor
public class Payment implements Serializable {
    private Long id;
    private String serial;
}