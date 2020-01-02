/**
 * Copyright (c) 2015, Alex All Rights Reserved.
 * 
 */  
package org.chl.logic.event.struct.net;

import org.chl.logic.user.bo.UserBo;

/**
 * ChannelCloseEvent
 * 
 * @author Alex
 * @date 2017年4月9日 下午5:50:34
 */
public class ChannelCloseEvent {

	public final UserBo userBo;
	
	public ChannelCloseEvent(UserBo userBo) {
		this.userBo = userBo;
	}
}
