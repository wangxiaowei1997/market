package com.xiaowei.market.redis.key;

public interface KeyPrefix {

    public int expireSeconds() ;

    public String getPrefix() ;

}
