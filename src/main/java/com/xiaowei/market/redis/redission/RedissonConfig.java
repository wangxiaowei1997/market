package com.xiaowei.market.redis.redission;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author wang wei
 * 2020/4/24 17:43
 */
public class RedissonConfig {


    private static final String host = "192.168.130.128";

    private static RedissonClient redissonClient;


    public static RedissonClient getRedisson() {
        if (redissonClient != null) {
            return redissonClient;
        }
        Config config = new Config();
        //单机模式  依次设置redis地址和密码
        config.useSingleServer().
                setAddress("redis://" + host + ":6379").
                setPassword("你的密码");
        redissonClient = Redisson.create(config);

        return redissonClient;
    }

}
