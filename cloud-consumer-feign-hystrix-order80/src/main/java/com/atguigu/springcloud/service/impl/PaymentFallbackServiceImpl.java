package com.atguigu.springcloud.service.impl;

import com.atguigu.springcloud.service.PaymentHystrixService;
import org.springframework.stereotype.Component;

/**
 * @author： chenr
 * @date： Created on 2021/7/5 16:37
 * @version： v1.0
 * @modified By:
 */
@Component
public class PaymentFallbackServiceImpl implements PaymentHystrixService {
    @Override
    public String paymentInfo_OK(Integer id) {
        return "--------PaymentFallbackServiceImpl fallback  paymentInfo_OK, o(╥﹏╥)o ";
    }

    @Override
    public String paymentInfo_TimeOut(Integer id) {
        return "--------PaymentFallbackServiceImpl fallback  paymentInfo_TimeOut, o(╥﹏╥)o ";
    }
}
