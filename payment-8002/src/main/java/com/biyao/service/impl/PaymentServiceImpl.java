package com.biyao.service.impl;

import com.biyao.dao.PaymentDao;
import com.biyao.entities.Payment;
import com.biyao.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Title: PaymentServiceImpl
 * Description: PaymentServiceImpl
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
@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentDao paymentDao;

    @Override
    public int create(Payment payment) {
        return paymentDao.create(payment);
    }

    @Override
    public Payment getPaymentById(Long id) {
        return paymentDao.getPaymentById(id);
    }
} 