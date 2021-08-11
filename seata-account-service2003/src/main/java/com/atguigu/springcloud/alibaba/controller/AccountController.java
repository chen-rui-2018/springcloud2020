package com.atguigu.springcloud.alibaba.controller;

import com.atguigu.springcloud.alibaba.domain.CommonResult;
import com.atguigu.springcloud.alibaba.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * @author： chenr
 * @date： Created on 2021/8/11 14:41
 * @version： v1.0
 * @modified By:
 */
@RestController
public class AccountController {
    @Autowired
    private AccountService accountService;


    /**
     * 扣减 余额
     *
     * @param userId
     * @param money
     * @return
     */
    @PostMapping(value = "/account/decreaseMoney")
    public CommonResult decreaseMoney(@RequestParam("userId") Long userId, @RequestParam("money") BigDecimal money) {
        accountService.decreaseMoney(userId, money);
        return new CommonResult(200, "扣减余额完成");
    }
}
