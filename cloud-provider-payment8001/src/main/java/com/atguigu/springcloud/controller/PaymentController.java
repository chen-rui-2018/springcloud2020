package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import com.atguigu.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author： chenr
 * @date： Created on 2021/6/21 17:35
 * @version： v1.0
 * @modified By:
 */
@Slf4j
@RestController
@RequestMapping("/payment")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;
    @PostMapping("/save")
    public CommonResult<Integer> create(@RequestBody  Payment payment){
        int result = paymentService.save(payment);
        log.info("插入结果:"+payment);
        return new CommonResult<Integer>(200,"新增成功!",result);
    }
    @GetMapping("/getPaymentById")
    public CommonResult<Payment> getPaymentById(long id){
        Payment result = paymentService.getPaymentById(id);
        return new CommonResult<Payment>(200,"查询成功!",result);
    }

}
