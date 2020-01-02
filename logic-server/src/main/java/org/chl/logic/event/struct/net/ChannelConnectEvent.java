package org.chl.logic.event.struct.net;

import org.chl.logic.user.bo.UserBo;

/**
 * 建立网络链接事件
 * 
 * @author Alex
 * @date 2017年4月9日 下午5:47:04
 */
public class ChannelConnectEvent {

	public final UserBo userBo;
	
	public ChannelConnectEvent(UserBo userBo) {
		this.userBo = userBo;
	}
}
