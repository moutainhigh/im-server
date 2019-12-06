package org.chl.common.mq.rabbit.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
	// email
	public static final String QUEUE_EMAIL = "queue-email";
	public static final String EXCHANGE_EMAIL = "exchange-email";
	public static final String ROUTINGKEY_EMAIL = "routingkey-email";

	@Bean
	Queue queueEmail() {
		return new Queue(QUEUE_EMAIL, false);
	}

	@Bean
	DirectExchange exchangeEmail() {
		return new DirectExchange(EXCHANGE_EMAIL);
	}

	@Bean
	Binding bindingEmail() {
		return BindingBuilder.bind(queueEmail()).to(exchangeEmail()).with(ROUTINGKEY_EMAIL);
	}
}
