package org.chl.common.mq.rabbit.producer;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.chl.common.mq.rabbit.config.RabbitConfig;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailProducer implements MQProducer<Object> {

	@Autowired
	private AmqpTemplate amqpTemplate;

	@Override
	public void sendMsg(Object msg) {
		log.info("email send:[{}]", JSONObject.toJSONString(msg));
		amqpTemplate.convertAndSend(RabbitConfig.EXCHANGE_EMAIL, RabbitConfig.ROUTINGKEY_EMAIL, msg);
	}

}
