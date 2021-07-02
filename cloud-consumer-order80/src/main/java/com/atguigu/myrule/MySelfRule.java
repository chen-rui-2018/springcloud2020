package com.atguigu.myrule;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author： chenr
 * @date： Created on 2021/7/1 17:12
 * @version： v1.0
 * @modified By:
 */
@Configuration
public class MySelfRule {
    @Bean
    public IRule myRule(){
        return  new RandomRule();
    }
}
