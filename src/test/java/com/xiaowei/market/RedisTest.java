package com.xiaowei.market;


import com.xiaowei.market.redis.key.OrderKey;
import com.xiaowei.market.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * @author wang wei
 * 2020/4/28 10:43
 */
@Slf4j
public class RedisTest extends BaseTest {

    @Resource
    RedisService redisService;

    @Test
    public void testSetEx(){
        log.info("LLLLLLLLLLLLL");
        redisService.set(OrderKey.getMiaoshaOrderByUidGid,""+"17803875680"+"_"+1,"AAAAAAAA") ;
    }
}
