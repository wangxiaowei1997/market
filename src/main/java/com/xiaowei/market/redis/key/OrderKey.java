package com.xiaowei.market.redis.key;

public class OrderKey extends BasePrefix {

    public OrderKey(int expireSeconds,String prefix) {
        super( expireSeconds,prefix);
    }

    //这里的过期时间应该设置为永久,否则几秒后记录就没了
    public static OrderKey getMiaoshaOrderByUidGid = new OrderKey(-1,"moug");

    public static OrderKey getOrderList = new OrderKey(5,"ol");

}
