package com.atguigu.springcloud.service;

import com.atguigu.springcloud.entities.Payment;

/**
 * @author： chenr
 * @date： Created on 2021/6/21 17:33
 * @version： v1.0
 * @modified By:
 */
public interface PaymentService {

    int save(Payment payment);
    Payment getPaymentById(long id);
}
