package com.xiaowei.market.rabbitmq;

import com.xiaowei.market.redis.service.RedisService;
import com.xiaowei.market.bean.vo.MiaoShaMessageVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQSender {

	private static Logger log = LoggerFactory.getLogger(MQSender.class);
	
	@Autowired
    AmqpTemplate amqpTemplate ;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	public void sendMiaoshaMessage(MiaoshaMessage mm) {
		String msg = RedisService.beanToString(mm);
		log.info("send message:"+msg);
		amqpTemplate.convertAndSend(MQConfig.MIAOSHA_QUEUE, msg);
	}

	/**
	 * 站内信
	 * @param mm
	 */
	public void sendMessage(MiaoshaMessage mm) {
//		String msg = RedisService.beanToString(mm);
		log.info("send message:"+"11111");
		rabbitTemplate.convertAndSend(MQConfig.EXCHANGE_TOPIC,"miaosha_*", "111111111");
	}

    /**
     * 站内信
     * @param
     */
    public void sendRegisterMessage(MiaoShaMessageVo miaoShaMessageVo) {
		String msg = RedisService.beanToString(miaoShaMessageVo);
        log.info("send message:{}" , msg);
		rabbitTemplate.convertAndSend(MQConfig.MIAOSHATEST,msg);
//        rabbitTemplate.convertAndSend(MQConfig.EXCHANGE_TOPIC,"miaosha_*", msg);
    }

	/**
	 * 自动关单
	 * 在发送创建订单消息的时候一并发出到A队列，
	 * A队列等待10分钟之后发送到死信队列。
	 */
	public void sendCloseOrderMessage(MiaoshaMessage mm) {
		String msg = RedisService.beanToString(mm);
		log.info("send message:"+msg);
		//配置消息过期时间30s
		MessagePostProcessor messagePostProcessor = message -> {
			MessageProperties messageProperties = message.getMessageProperties();
			messageProperties.setExpiration("30000");
			return message;
		};
		amqpTemplate.convertAndSend(MQConfig.MS_DLX_MAKE_EXCHANGE,"james", msg,messagePostProcessor);
	}
}
