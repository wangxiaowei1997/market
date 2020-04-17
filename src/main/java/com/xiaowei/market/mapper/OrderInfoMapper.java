package com.xiaowei.market.mapper;

import com.xiaowei.market.bean.db.OrderInfo;
import com.xiaowei.market.domain.MiaoshaOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderInfoMapper extends BaseMapper<OrderInfo> {

    /**
     * 通过用户id和商品id查询秒杀订单
     */
    MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(@Param("userNickName") long userNickName,
                                                @Param("goodsId") long goodsId);


    /**
     * 插入秒杀订单(完整订单信息)
     */
    long insert(OrderInfo orderInfo);

    /**
     * 插入秒杀订单(简单信息)
     */
    int insertMiaoshaOrder(MiaoshaOrder miaoshaOrder);

    /**
     * 通过订单id查询订单
     */
    OrderInfo getOrderById(@Param("orderId") long orderId);

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