/*
 * Copyright (c) 2016, Alex. All Rights Reserved.
 */

package org.chl.logic.event.manager;

import com.google.common.eventbus.EventBus;
import org.chl.logic.event.core.LoggingSubscriberExceptionHandler;
import org.springframework.stereotype.Component;

/**
 * 基于google EventBus的事件管理器
 *
 * @author Alex
 * @date 2016/8/3 16:48
 */
@Component
public class EventMgr extends EventBus {

	public EventMgr() {
		// guava
		// EventBus的默认发布事件处理异常处理器LoggingSubscriberExceptionHandler没有记录异常堆栈，不能很好的定位问题
		super(new LoggingSubscriberExceptionHandler());
	}
}
