package com.atguigu.springcloud.alibaba.service.impl;

import com.atguigu.springcloud.alibaba.dao.AccountDao;
import com.atguigu.springcloud.alibaba.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;
import java.util.zip.Inflater;

/**
 * @author： chenr
 * @date： Created on 2021/8/11 14:40
 * @version： v1.0
 * @modified By:
 */
@Service
@Slf4j
public class AccountServiceImpl  implements AccountService {
    @Autowired
    private AccountDao accountDao;

    @Override
    public void decreaseMoney(Long userId, BigDecimal money) {
        log.info("扣减 余额开始");
        try{
            TimeUnit.SECONDS.sleep(20);
        }catch (Exception e){
            e.printStackTrace();
        }
        accountDao.decreaseMoney(userId,money);
        log.info("扣减 余额结束");
    }
}
