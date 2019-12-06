package org.chl.common.mq.rabbit.consumer;

import org.springframework.amqp.core.Message;

public interface MQConsumer {

	void receiveMs(Message msg);
}
