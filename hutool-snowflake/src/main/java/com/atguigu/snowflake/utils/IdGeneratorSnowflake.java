package com.atguigu.snowflake.utils;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author： chenr
 * @date： Created on 2021/8/13 15:27
 * @version： v1.0
 * @modified By:
 */
@Component
@Slf4j
public class IdGeneratorSnowflake {
    private long  workId = 0;
    private long  dataCenterId = 1;
    private Snowflake snowflake = IdUtil.createSnowflake(workId,dataCenterId);

    @PostConstruct
    public void  init(){
        try {
            workId = NetUtil.ipv4ToLong(NetUtil.getLocalhostStr());
            log.info("当前机器的 workId: " + workId);
        }catch (Exception e){
            e.printStackTrace();
            log.warn("当前机器的workerId 获取失败!",e);
            workId = NetUtil.getLocalhost().hashCode();
        }
    }
    public synchronized  long snowflakeId(){
        return snowflake.nextId();
    }
    public synchronized  long snowflakeId(long workId,long dataCenterId){
        Snowflake snowflake = IdUtil.createSnowflake(workId,dataCenterId);
        return snowflake.nextId();
    }

    public static void main(String[] args) {
        long l = new IdGeneratorSnowflake().snowflakeId();
        System.out.println(l);
    }
}
