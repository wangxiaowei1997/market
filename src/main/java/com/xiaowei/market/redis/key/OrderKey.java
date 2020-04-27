package com.xiaowei.market.redis.key;

public class OrderKey extends BasePrefix {

    public OrderKey(int expireSeconds,String prefix) {
        super( expireSeconds,prefix);
    }


    public static OrderKey getMiaoshaOrderByUidGid = new OrderKey(5,"moug");

    public static OrderKey getOrderList = new OrderKey(5,"ol");

}
