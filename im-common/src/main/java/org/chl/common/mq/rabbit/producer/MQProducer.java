package org.chl.common.mq.rabbit.producer;

public interface MQProducer<T> {

	void sendMsg(T msg);
}
