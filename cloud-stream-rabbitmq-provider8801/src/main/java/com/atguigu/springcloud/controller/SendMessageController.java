package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.service.IMessageProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author： chenr
 * @date： Created on 2021/7/14 16:54
 * @version： v1.0
 * @modified By:
 */
@RestController
public class SendMessageController {
    @Resource
    private IMessageProvider messageProvider;

   @GetMapping("/sendMessage")
    public String send(){
       return messageProvider.send();

   }
}
