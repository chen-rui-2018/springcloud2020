package com.atguigu.springcloud.alibaba.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * @author： chenr
 * @date： Created on 2021/8/11 14:31
 * @version： v1.0
 * @modified By:
 */
@Mapper
public interface AccountDao {
    /**
     * 扣减 账户余额
     * @param userId
     * @param money
     */
    void decreaseMoney(@Param("userId") Long userId,@Param("money") BigDecimal money);
}
