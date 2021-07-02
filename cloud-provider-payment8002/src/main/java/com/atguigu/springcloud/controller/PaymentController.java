package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import com.atguigu.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

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

    @Value("${server.port}")
    private String serverPort;

    @PostMapping("/create")
    public CommonResult<Integer> create(@RequestBody  Payment payment){
        int result = paymentService.save(payment);
        log.info("插入结果:"+payment);
        return new CommonResult<Integer>(200,"新增成功!,serverPort:"+serverPort,result);
    }
    @GetMapping("/getPaymentById/{id}")
    public CommonResult<Payment> getPaymentById(@PathVariable long id){
        Payment result = paymentService.getPaymentById(id);
        if (result != null) {
        return new CommonResult<Payment>(200,"查询成功!,serverPort:"+serverPort,result);
        } else {
            return new CommonResult<Payment>(444,"没有对应的记录!,serverPort:"+serverPort,result);
        }

    }

    @GetMapping("/lb")
    public String getPayment(){
        return serverPort;
    }

    @GetMapping("/feign/timeout")
    public String paymentFeignTimeout() {

        try {
            TimeUnit.SECONDS.sleep(3);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return serverPort;
    }
}
