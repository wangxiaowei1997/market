package com.xiaowei.market.rabbitmq;

import com.xiaowei.market.bean.db.MiaoshaOrder;
import com.xiaowei.market.bean.db.MiaoshaUser;
import com.xiaowei.market.redis.service.RedisService;
import com.xiaowei.market.service.GoodsService;
import com.xiaowei.market.service.MiaoShaMessageService;
import com.xiaowei.market.service.MiaoshaService;
import com.xiaowei.market.service.OrderService;
import com.xiaowei.market.bean.vo.GoodsVo;
import com.xiaowei.market.bean.vo.MiaoShaMessageVo;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Service
public class MQReceiver {

		private static Logger log = LoggerFactory.getLogger(MQReceiver.class);
		
		@Autowired
		RedisService redisService;
		
		@Autowired
		GoodsService goodsService;
		
		@Autowired
		OrderService orderService;
		
		@Autowired
		MiaoshaService miaoshaService;

		@Autowired
		MiaoShaMessageService messageService ;
		
		@RabbitListener(queues=MQConfig.MIAOSHA_QUEUE)
		public void receive(Message message,Channel channel) throws Exception {
			log.info("receive message:"+message);
			String messageBody =  new String(message.getBody(), "UTF-8");
			MiaoshaMessage mm  = RedisService.stringToBean(messageBody, MiaoshaMessage.class);
			MiaoshaUser user = mm.getUser();
			long goodsId = mm.getGoodsId();

			GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
	    	int stock = goods.getStockCount();
	    	if(stock <= 0) {
	    		log.info("{} 库存空了",goodsId+"_"+user.getNickname());
				channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
				return;
	    	}
	    	//判断是否已经秒杀到了
	    	MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(Long.valueOf(user.getNickname()), goodsId);
	    	if(order != null) {
	    		log.info("{}已经秒杀到了",goodsId+"_"+user.getNickname());
				channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
				return;
	    	}
	    	//减库存 下订单 写入秒杀订单
	    	miaoshaService.miaosha(user, goods);
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
		}


    @RabbitListener(queues = MQConfig.MIAOSHATEST)
    public void receiveMiaoShaMessage(Message message, Channel channel) throws IOException {
        log.info("接受到的消息为:{}", message);
        String messRegister = new String(message.getBody(), "UTF-8");
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
        MiaoShaMessageVo msm = RedisService.stringToBean(messRegister, MiaoShaMessageVo.class);
        messageService.insertMs(msm);
    }

    /**
     * 从死信队列中获取过期的订单消息，如果订单未支付,关闭订单
     */
    @RabbitListener(queues = MQConfig.MS_DLX_ACCEPT_QUEUE)
    public void receiveMSCloseOrderMessage(Message message, Channel channel) throws Exception {
        log.info("接受到的消息为:{}", message);
        String messageBody = new String(message.getBody(), "UTF-8");
        log.info("测试死信队列成功!");
		MiaoshaMessage mm  = RedisService.stringToBean(messageBody, MiaoshaMessage.class);
		MiaoshaUser user = mm.getUser();
		long goodsId = mm.getGoodsId();
		GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
		miaoshaService.closeOrder(user,goods);
		channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
		log.info("自动关单成功");
	}


}
