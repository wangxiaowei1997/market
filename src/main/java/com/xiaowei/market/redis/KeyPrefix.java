package com.xiaowei.market.redis;

public interface KeyPrefix {

    public int expireSeconds() ;

    public String getPrefix() ;

}
