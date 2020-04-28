package com.xiaowei.market.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class MQConfig {

	/**
	 * /usr/sbin/rabbitmq-plugins enable rabbitmq_management
	 * mq页面
	 */
	public static final String MIAOSHA_QUEUE = "miaosha.queue";

	public static final String EXCHANGE_TOPIC = "exchange_topic";

	public static final String MIAOSHA_MESSAGE = "miaosha_mess";

	public static final String MIAOSHATEST = "miaoshatest";

	public static final String QUEUE = "queue";
	public static final String TOPIC_QUEUE1 = "topic.queue1";
	public static final String TOPIC_QUEUE2 = "topic.queue2";
	public static final String HEADER_QUEUE = "header.queue";
	public static final String TOPIC_EXCHANGE = "topicExchage";
	public static final String FANOUT_EXCHANGE = "fanoutxchage";
	public static final String HEADERS_EXCHANGE = "headersExchage";

	public static final String MS_DLX_ACCEPT_QUEUE = "ms_dlx_accept_queue";
	public static final String MS_DLX_ACCEPT_EXCHANGE = "ms_dlx_accept_exchange";
	public static final String MS_DLX_MAKE_QUEUE = "ms_dlx_make_queue";
	public static final String MS_DLX_MAKE_EXCHANGE = "ms_dlx_make_exchange";
	
	/**
	 * Direct模式 交换机Exchange
	 * */
	@Bean
	public Queue queue() {
		return new Queue(QUEUE, true);
	}
	
	/**
	 * Topic模式 交换机Exchange
	 * */
	@Bean
	public Queue topicQueue1() {
		return new Queue(TOPIC_QUEUE1, true);
	}
	@Bean
	public Queue topicQueue2() {
		return new Queue(TOPIC_QUEUE2, true);
	}
	@Bean
	public TopicExchange topicExchage(){
		return new TopicExchange(TOPIC_EXCHANGE);
	}
	@Bean
	public Binding topicBinding1() {
		return BindingBuilder.bind(topicQueue1()).to(topicExchage()).with("topic.key1");
	}
	@Bean
	public Binding topicBinding2() {
		return BindingBuilder.bind(topicQueue2()).to(topicExchage()).with("topic.#");
	}
	/**
	 * Fanout模式 交换机Exchange
	 * */
	@Bean
	public FanoutExchange fanoutExchage(){
		return new FanoutExchange(FANOUT_EXCHANGE);
	}
	@Bean
	public Binding FanoutBinding1() {
		return BindingBuilder.bind(topicQueue1()).to(fanoutExchage());
	}
	@Bean
	public Binding FanoutBinding2() {
		return BindingBuilder.bind(topicQueue2()).to(fanoutExchage());
	}
	/**
	 * Header模式 交换机Exchange
	 * */
	@Bean
	public HeadersExchange headersExchage(){
		return new HeadersExchange(HEADERS_EXCHANGE);
	}
	@Bean
	public Queue headerQueue1() {
		return new Queue(HEADER_QUEUE, true);
	}
	@Bean
	public Binding headerBinding() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("header1", "value1");
		map.put("header2", "value2");
		return BindingBuilder.bind(headerQueue1()).to(headersExchage()).whereAll(map).match();
	}


	/**
	 * 死信队列测试---发送交换机
	 */
	@Bean
	public TopicExchange msDlxMakeExchange(){
		return new TopicExchange(MS_DLX_MAKE_EXCHANGE);
	}

	/**
	 * 死信队列测试---发送queue
	 */
	@Bean
	public Queue msDlxMakeQueue(){
		Map<String,Object> args = new HashMap<String,Object>();
		//TODO 绑定死信接收交换器
		args.put("x-dead-letter-exchange", MS_DLX_ACCEPT_EXCHANGE);
		//TODO 死信路由键，会替换消息原来的路由键
		args.put("x-dead-letter-routing-key", "deal");
		return new Queue(MS_DLX_MAKE_QUEUE,false,true, false, args);
	}

	/**
	 * 死信队列测试---接收交换机
	 */
	@Bean
	public TopicExchange msDlxAcceptExchange(){
		return new TopicExchange(MS_DLX_ACCEPT_EXCHANGE);
	}

	/**
	 * 死信队列测试---接收queue
	 */
	@Bean
	public Queue msDlxAcceptQueue(){
		return new Queue(MS_DLX_ACCEPT_QUEUE);
	}


	/**
	 * 将发送交换机和发送队列绑定
	 */
	@Bean
	public Binding topicBindingDlx1(){

		return BindingBuilder.bind(msDlxMakeQueue()).to(msDlxMakeExchange()).with("#");
	}

	/**
	 * 绑定接收交换机和接收队列
	 */
	@Bean
	public Binding topicBindingDlx2(){
		return BindingBuilder.bind(msDlxAcceptQueue()).to(msDlxAcceptExchange()).with("#");

	}


}
