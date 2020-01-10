/*
 * Copyright (c) 2016, Alex. All Rights Reserved.
 */

package org.chl.logic.event.core;

import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * EventBus的默认发布事件处理异常处理器LoggingSubscriberExceptionHandler没有记录异常堆栈，不能很好的定位问题
 * 
 * @author Alex
 * @date 2016/8/3 16:47
 */
@Slf4j
public class LoggingSubscriberExceptionHandler implements SubscriberExceptionHandler {

	@Override
	public void handleException(Throwable exception, SubscriberExceptionContext context) {
		log.error("Could not dispatch event: " + context.getSubscriber() + " to " + context.getSubscriberMethod(),exception);
	}
}
