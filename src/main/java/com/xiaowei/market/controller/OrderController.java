package com.xiaowei.market.controller;

import com.xiaowei.market.bean.db.MiaoshaUser;
import com.xiaowei.market.bean.db.OrderInfo;
import com.xiaowei.market.common.resultbean.ResultGeekQ;
import com.xiaowei.market.redis.RedisService;
import com.xiaowei.market.service.GoodsService;
import com.xiaowei.market.service.MiaoShaUserService;
import com.xiaowei.market.service.OrderService;
import com.xiaowei.market.bean.vo.GoodsVo;
import com.xiaowei.market.bean.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.xiaowei.market.common.enums.ResultStatus.ORDER_NOT_EXIST;
import static com.xiaowei.market.common.enums.ResultStatus.SESSION_ERROR;

@Controller
@RequestMapping("/order")
public class OrderController {

	@Autowired
	MiaoShaUserService userService;
	
	@Autowired
	RedisService redisService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	GoodsService goodsService;
	
    @RequestMapping("/detail")
    @ResponseBody
    public ResultGeekQ<OrderDetailVo> info(Model model, MiaoshaUser user,
                                           @RequestParam("orderId") long orderId) {
		ResultGeekQ<OrderDetailVo> result = ResultGeekQ.build();
		if (user == null) {
			result.withError(SESSION_ERROR.getCode(), SESSION_ERROR.getMessage());
			return result;
		}
    	OrderInfo order = orderService.getOrderById(orderId);
    	if(order == null) {
			result.withError(ORDER_NOT_EXIST.getCode(), ORDER_NOT_EXIST.getMessage());
			return result;
    	}
    	long goodsId = order.getGoodsId();
    	GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
    	OrderDetailVo vo = new OrderDetailVo();
    	vo.setOrder(order);
    	vo.setGoods(goods);
    	result.setData(vo);
    	return result;
    }
    
}
