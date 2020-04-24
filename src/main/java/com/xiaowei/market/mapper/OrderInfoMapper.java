package com.xiaowei.market.mapper;

import com.xiaowei.market.bean.db.OrderInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderInfoMapper extends BaseMapper<OrderInfo> {


    /**
     * 插入秒杀订单(完整订单信息)
     */
    long insert(OrderInfo orderInfo);


    /**
     * 通过订单id查询订单
     */
    OrderInfo getOrderById(@Param("orderId") long orderId);

    /**
     * 查询用户名下的订单列表
     */
    List<OrderInfo> getOrderList(@Param("userId") long userId);

    /**
     *通过状态和创建时间查询订单
     */
    List<OrderInfo> selectOrderStatusByCreateTime(@Param("status") Integer status,
                                                  @Param("createDate") String createDate);
    /**
     * 通过订单信息关闭订单
     */
    int closeOrderByOrderInfo();

}