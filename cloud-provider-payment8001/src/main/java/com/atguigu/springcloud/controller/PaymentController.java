package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import com.atguigu.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @Autowired
    private DiscoveryClient discoveryClient;

    @Value("${server.port}")
    private String serverPort;

    @PostMapping("/create")
    public CommonResult<Integer> create(@RequestBody Payment payment) {
        int result = paymentService.save(payment);
        log.info("插入结果:" + payment);
        return new CommonResult<Integer>(200, "新增成功!,serverPort:" + serverPort, result);
    }

    @GetMapping("/getPaymentById/{id}")
    public CommonResult<Payment> getPaymentById(@PathVariable long id) {
        Payment result = paymentService.getPaymentById(id);
        if (result != null) {
            return new CommonResult<Payment>(200, "查询成功!,serverPort:" + serverPort, result);
        } else {
            return new CommonResult<Payment>(444, "没有对应的记录!,serverPort:" + serverPort, result);
        }

    }

    @GetMapping("/discovery")
    public Object discovery() {
        List<String> services = discoveryClient.getServices();
        for (String service : services) {
            log.info("service:" + service);
        }
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        for (ServiceInstance instance : instances) {
            log.info(instance.getServiceId() + "\t" + instance.getHost() + "\t" + instance.getPort() + "\t" + instance.getUri());
        }
        return this.discoveryClient;
    }
    @GetMapping("/lb")
    public String getPayment(){
        return serverPort;
    }

}
