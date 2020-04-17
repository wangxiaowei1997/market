package com.xiaowei.market.redis.redismanager;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Slf4j
public class RedisManager {

    private static JedisPool jedisPool;

    static {
        String host = "192.168.130.128";
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxWaitMillis(100);
        jedisPoolConfig.setMaxIdle(8);
        jedisPoolConfig.setMinIdle(0);
        jedisPoolConfig.setMaxTotal(8);
        jedisPool = new JedisPool(jedisPoolConfig, host);
    }

    public static Jedis getJedis() throws Exception{
        if(null!=jedisPool){
            log.info("getNumIdle ={}",jedisPool.getNumActive());
            return jedisPool.getResource();
        }
        throw new Exception("Jedispool was not init !!!");
    }

    public static void returnJedis(Jedis jedis){
        if (jedis != null) {
            jedisPool.returnResource(jedis);
        }
    }


}
