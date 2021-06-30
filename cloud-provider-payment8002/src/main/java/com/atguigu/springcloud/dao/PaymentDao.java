package com.atguigu.springcloud.dao;

import com.atguigu.springcloud.entities.Payment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author： chenr
 * @date： Created on 2021/6/18 17:42
 * @version： v1.0
 * @modified By:
 */
@Mapper
public interface PaymentDao {
    int save(Payment payment);
    Payment getPaymentById(@Param("id") long id);
}
