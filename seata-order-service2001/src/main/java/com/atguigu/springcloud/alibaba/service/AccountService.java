package com.atguigu.springcloud.alibaba.service;

import com.atguigu.springcloud.alibaba.domain.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

/**
 * @author： chenr
 * @date： Created on 2021/8/11 10:56
 * @version： v1.0
 * @modified By:
 */
@FeignClient(value = "seata-account-service")
public interface AccountService {
    /**
     * 扣减 余额
     * @param userId
     * @param money
     * @return
     */
    @PostMapping(value = "/account/decreaseMoney")
    CommonResult decreaseMoney(@RequestParam("userId") Long userId, @RequestParam("money") BigDecimal money);
}
