package com.atguigu.snowflake.service.impl;

import com.atguigu.snowflake.service.SnowFlakeService;
import com.atguigu.snowflake.utils.IdGeneratorSnowflake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author： chenr
 * @date： Created on 2021/8/13 15:21
 * @version： v1.0
 * @modified By:
 */
@Service
public class SnowFlakeServiceImpl implements SnowFlakeService {

    @Autowired
    private IdGeneratorSnowflake idGenerator;

    @Override
    public String getIdBySnowFlake() {
        ExecutorService threadPool = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 20; i++) {

            threadPool.submit(()->{
                long l = idGenerator.snowflakeId();
                System.out.println(l);
            });

        }

        return "hello snowflake";
    }
}
