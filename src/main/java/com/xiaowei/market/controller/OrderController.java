package com.xiaowei.market.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.xiaowei.market.bean.db.MiaoshaUser;
import com.xiaowei.market.bean.db.OrderInfo;
import com.xiaowei.market.common.resultbean.ResultGeekQ;
import com.xiaowei.market.redis.GoodsKey;
import com.xiaowei.market.redis.OrderKey;
import com.xiaowei.market.redis.RedisService;
import com.xiaowei.market.service.GoodsService;
import com.xiaowei.market.service.MiaoShaUserService;
import com.xiaowei.market.service.OrderService;
import com.xiaowei.market.bean.vo.GoodsVo;
import com.xiaowei.market.bean.vo.OrderDetailVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.xiaowei.market.common.enums.ResultStatus.ORDER_NOT_EXIST;
import static com.xiaowei.market.common.enums.ResultStatus.SESSION_ERROR;

@Slf4j
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
	@Resource
	BaseController baseController;
	
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

	@RequestMapping("/getOrderList")
	@ResponseBody
    public ResultGeekQ<List<OrderDetailVo>> getOrderList(Model model, MiaoshaUser user){
		ResultGeekQ<List<OrderDetailVo>> result = ResultGeekQ.build();
		if (user == null) {
			result.withError(SESSION_ERROR.getCode(), SESSION_ERROR.getMessage());
			return result;
		}

		result.setData(getOrderList(user));

		return result;
	}


	@RequestMapping(value="/to_list", produces="text/html")
	@ResponseBody
	public String list(HttpServletRequest request, HttpServletResponse response, Model model, MiaoshaUser user) {
		model.addAttribute("user", user);
		model.addAttribute("orderList", getOrderList(user));

		return baseController.render(request,response,model,"order_list", OrderKey.getOrderList,"");
	}


	private List<OrderDetailVo> getOrderList(MiaoshaUser user){
    	log.info("user={}",user);
		List<OrderInfo> orderList = orderService.getOrderList(Long.valueOf(user.getNickname()));
		log.info("orderList={}", JSONObject.toJSONString(orderList));
		List<OrderDetailVo> resultList = Lists.newArrayList();
		for(OrderInfo o:orderList){
			long goodsId = o.getGoodsId();
			GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
			OrderDetailVo vo = new OrderDetailVo();
			vo.setOrder(o);
			vo.setGoods(goods);
			resultList.add(vo);
		}

		log.info("resultList={}",JSONObject.toJSONString(resultList));
		return resultList;
	}
    
}
