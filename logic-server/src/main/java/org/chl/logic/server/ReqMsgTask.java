package org.chl.logic.server;

import com.google.protobuf.InvalidProtocolBufferException;
import org.chl.logic.user.bo.UserBo;
import org.chl.message.CommMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 消息处理task
 */
public class ReqMsgTask implements Runnable {

    // 消息处理超时日志
    private static final Logger MSG_HANDLE_OVERTIME_LOG = LoggerFactory.getLogger("MsgHandleOvertimeLog");
    // 消息异常日志
    private static final Logger MSG_HANDLE_EXCEPTION_LOG = LoggerFactory.getLogger("MsgHandleExceptionLog");
    private final UserBo usrBo;
    private final CommMessage.Comm msg;

    public ReqMsgTask(UserBo usrBo, CommMessage.Comm msg) {
        this.usrBo = usrBo;
        this.msg = msg;
    }

    @Override
    public void run() {
        try {
            long now = System.currentTimeMillis();
            MsgHandlerFactory.getHandler(msg.getMsgId()).action(usrBo, msg);
            long duration = System.currentTimeMillis() - now;
            // 超过10毫秒的操作
            if (duration > 10) {
                MSG_HANDLE_OVERTIME_LOG.warn("消息[{}]处理时间[{}]", msg.getMsgId(), duration);
            }
        } catch (InvalidProtocolBufferException e) {
            if (usrBo.getUser() != null) {
                MSG_HANDLE_EXCEPTION_LOG.error("玩家[" + usrBo.getId() + "]会话[ " + usrBo.channel + "]请求消息[" + msg + "]执行异常", e);
            } else {
                MSG_HANDLE_EXCEPTION_LOG.error("会话[ " + usrBo.channel + "]请求消息[" + msg + "]执行异常", e);
            }
        }
    }
}
