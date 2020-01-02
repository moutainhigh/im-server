package org.chl.logic.server;

import com.google.protobuf.InvalidProtocolBufferException;
import org.chl.logic.user.bo.UserBo;
import org.chl.message.CommMessage;

/**
 * 消息处理接口
 */
public interface Ihandler {
    void action(UserBo usrBo, CommMessage.Comm msg) throws InvalidProtocolBufferException;
}
