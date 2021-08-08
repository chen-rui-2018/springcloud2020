package com.atguigu.springcloud.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.atguigu.springcloud.service.impl.ResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author： chenr
 * @date： Created on 2021/7/28 10:45
 * @version： v1.0
 * @modified By:
 */
@RestController
@Slf4j
public class FlowLimitController {
    @Autowired
    private ResourceService resourceService;


    @GetMapping("/testA")
    public String getTestA() {

        log.info(Thread.currentThread().getName()+"\t"+"+++++++++++++testA");
        return "===>testA";


    }

    @GetMapping("/testB")
    public String getTestB() {
        return "===>testB";
    }

    @GetMapping("/testD")
    public String getTestD() {
//        try {
//            TimeUnit.SECONDS.sleep(1);
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        }
//        log.info("testD 测试 RT ");
        log.info("testD 测试 异常比例 ");
        int age = 10/0;
        return "===>testD --- > RT";
    }   @GetMapping("/testE")
    public String getTestE() {
//        try {
//            TimeUnit.SECONDS.sleep(1);
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        }
//        log.info("testD 测试 RT ");
        log.info("testE 测试 异常数 ");
        int age = 10/0;
        return "===>testE 测试 异常数";
    }
    @GetMapping("/testHotKey")
    @SentinelResource(value = "testHotKey",blockHandler = "deal_testHotKey")
    public String getTestHotKey(@RequestParam(value = "p1",required = false) String p1,
                                @RequestParam(value = "p2",required = false) String p2) {

        log.info("testHotKey 测试 热点key ");

//          int x = 10/0;
        return "===>testHotKey 测试 热点key";
    }
    public String deal_testHotKey(String p1, String p2, BlockException exception){
        return "----deal_testHotKey , o(╥﹏╥)o";
    }
}
