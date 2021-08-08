package com.atguigu.springcloud.service.impl;

import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import com.atguigu.springcloud.service.PaymentService;
import org.springframework.stereotype.Component;

/**
 * @author： chenr
 * @date： Created on 2021/8/8 16:02
 * @version： v1.0
 * @modified By:
 */
@Component
public class PaymentFallbackService implements PaymentService {

    @Override
    public CommonResult<Payment> paymentSQL(Long id) {

        return new CommonResult<>(444,"服务降级返回,---PaymentFallbackService",new Payment(id,"error id"));
    }
}
