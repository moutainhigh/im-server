package org.chl.common.mq.rabbit.consumer;

import com.alibaba.fastjson.JSONObject;
import org.chl.common.email.service.EmailService;
import org.chl.common.model.PairModel;
import org.chl.common.mq.rabbit.config.RabbitConfig;
import org.chl.common.util.ObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailConsumer implements MQConsumer {

	private static final Logger LOG = LoggerFactory.getLogger(EmailConsumer.class);

	@Autowired
	private EmailService emailService;

	@RabbitListener(queues = RabbitConfig.QUEUE_EMAIL)
	@Override
	public void receiveMs(Message msg) {
		PairModel<?, ?> model = ObjectUtil.from(msg.getBody(), PairModel.class);
		if (null != model) {
			LOG.info("email receive:[{}]", JSONObject.toJSONString(model));
			emailService.sendEmail(model);
		}
	}
}
