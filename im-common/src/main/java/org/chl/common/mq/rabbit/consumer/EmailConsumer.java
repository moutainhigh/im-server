package org.chl.common.mq.rabbit.consumer;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.chl.common.email.service.EmailService;
import org.chl.common.model.PairModel;
import org.chl.common.mq.rabbit.config.RabbitConfig;
import org.chl.common.util.ObjectUtil;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailConsumer implements MQConsumer {

	@Autowired
	private EmailService emailService;

	@RabbitListener(queues = RabbitConfig.QUEUE_EMAIL)
	@Override
	public void receiveMs(Message msg) {
		PairModel<?, ?> model = ObjectUtil.from(msg.getBody(), PairModel.class);
		if (null != model) {
			log.info("email receive:[{}]", JSONObject.toJSONString(model));
			emailService.sendEmail(model);
		}
	}
}
