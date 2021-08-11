package com.atguigu.springcloud.alibaba.service;

import java.math.BigDecimal;

/**
 * @author： chenr
 * @date： Created on 2021/8/11 14:39
 * @version： v1.0
 * @modified By:
 */
public interface AccountService {

    void decreaseMoney( Long userId, BigDecimal money);
}
