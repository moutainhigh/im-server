package org.chl.common.mq.rabbit.producer;

import com.alibaba.fastjson.JSONObject;
import org.chl.common.mq.rabbit.config.RabbitConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailProducer implements MQProducer<Object> {

	private static final Logger LOG = LoggerFactory.getLogger(EmailProducer.class);

	@Autowired
	private AmqpTemplate amqpTemplate;

	@Override
	public void sendMsg(Object msg) {
		LOG.info("email send:[{}]", JSONObject.toJSONString(msg));
		amqpTemplate.convertAndSend(RabbitConfig.EXCHANGE_EMAIL, RabbitConfig.ROUTINGKEY_EMAIL, msg);
	}

}
