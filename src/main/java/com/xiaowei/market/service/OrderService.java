package com.xiaowei.market.service;

import com.xiaowei.market.bean.db.MiaoshaOrder;
import com.xiaowei.market.bean.db.MiaoshaUser;
import com.xiaowei.market.bean.db.OrderInfo;
import com.xiaowei.market.mapper.MiaoshaOrderMapper;
import com.xiaowei.market.mapper.OrderInfoMapper;
import com.xiaowei.market.redis.OrderKey;
import com.xiaowei.market.redis.RedisService;
import com.xiaowei.market.utils.DateTimeUtils;
import com.xiaowei.market.bean.vo.GoodsVo;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

import static com.xiaowei.market.common.Constanst.orderStaus.ORDER_NOT_PAY;

@Service
public class OrderService {
	
	@Resource
	OrderInfoMapper orderInfoMapper;
	@Resource
	MiaoshaOrderMapper miaoshaOrderMapper;

	@Autowired
	private RedisService redisService ;
	
	public MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(long userId, long goodsId) {
		return	redisService.get(OrderKey.getMiaoshaOrderByUidGid,""+userId+"_"+goodsId,MiaoshaOrder.class) ;
	}

	/**
	 * 根据订单id查询订单
	 */
	public OrderInfo getOrderById(long orderId) {
		return orderInfoMapper.getOrderById(orderId);
	}

	/**
	 * 查询用户名下的订单
	 */
	public List<OrderInfo> getOrderList(long userId){
		return orderInfoMapper.getOrderList(userId);
	}

	@Transactional
	public OrderInfo createOrder(MiaoshaUser user, GoodsVo goods) {
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setCreateDate(new Date());
		orderInfo.setDeliveryAddrId(0L);
		orderInfo.setGoodsCount(1);
		orderInfo.setGoodsId(goods.getId());
		orderInfo.setGoodsName(goods.getGoodsName());
		orderInfo.setGoodsPrice(goods.getMiaoshaPrice());
		orderInfo.setOrderChannel(1);
		orderInfo.setStatus(0);
		orderInfo.setUserId(Long.valueOf(user.getNickname()));
		orderInfoMapper.add(orderInfo);
		MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
		miaoshaOrder.setGoodsId(goods.getId());
		miaoshaOrder.setOrderId(orderInfo.getId());
		miaoshaOrder.setUserId(Long.valueOf(user.getNickname()));
		miaoshaOrderMapper.add(miaoshaOrder);
		redisService.set(OrderKey.getMiaoshaOrderByUidGid,""+user.getNickname()+"_"+goods.getId(),miaoshaOrder) ;
		return orderInfo;
	}

	public void closeOrder(int hour){
		Date closeDateTime = DateUtils.addHours(new Date(),-hour);
		List<OrderInfo> orderInfoList = orderInfoMapper.selectOrderStatusByCreateTime(Integer.valueOf(ORDER_NOT_PAY.ordinal()), DateTimeUtils.dateToStr(closeDateTime));
		for (OrderInfo orderInfo:orderInfoList){
			System.out.println("orderinfo  infomation "+orderInfo.getGoodsName());
		}
	}

	
}
