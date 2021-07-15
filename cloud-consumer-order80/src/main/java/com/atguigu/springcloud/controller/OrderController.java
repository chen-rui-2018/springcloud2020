package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import com.atguigu.springcloud.lb.LoadBalancer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

/**
 * @author： chenr
 * @date： Created on 2021/6/25 17:20
 * @version： v1.0
 * @modified By:
 */
@RestController
@Slf4j
@RequestMapping("/consumer")
public class OrderController {
//    public  static final  String PAYMENT_URL = "http://cloud-payment-service";
    public  static final  String PAYMENT_URL = "http://localhost:8001";

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private LoadBalancer loadBalancer;
    @Autowired
    private DiscoveryClient discoveryClient;


    @GetMapping("/payment/create")
    public CommonResult<Payment> createPayment(Payment payment){

        return restTemplate.postForObject(PAYMENT_URL+"/payment/create",payment,CommonResult.class);
    }
    @GetMapping("/payment/getPayment/{id}")
    public CommonResult<Payment> getPayment(@PathVariable long id){
        return restTemplate.getForObject(PAYMENT_URL+"/payment/getPaymentById/"+id,CommonResult.class);
    }
    @GetMapping("/payment/getForEntity/{id}")
    public CommonResult<Payment> getPayment2(@PathVariable long id){
        ResponseEntity<CommonResult> forEntity = restTemplate.getForEntity(PAYMENT_URL + "/payment/getPaymentById/" + id, CommonResult.class);
        if (forEntity.getStatusCode().is2xxSuccessful()) {
            return forEntity.getBody();
        }else {
            return new CommonResult<>(444,"获取操作失败!");
        }
    }
    @GetMapping("/payment/lb")
    public String getPaymentLB(){
        List<ServiceInstance> instanceList = discoveryClient.getInstances("cloud-payment-service");
        if(instanceList == null || instanceList.size() <=0){
            return "没有可用的服务";
        }

        ServiceInstance instances = loadBalancer.instances(instanceList);
        String uri = instances.getUri().toString();
        log.info("访问地址 uri:"+uri);
        return restTemplate.getForObject(uri + "/payment/lb", String.class);
    }
    @GetMapping("/payment/zipkin")
    public String paymentZipkin(){
       return restTemplate.getForObject(PAYMENT_URL+"/payment/zipkin",String.class);
    }

}
