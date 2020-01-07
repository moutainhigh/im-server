package org.chl.logic.akka.actor.login;

import akka.actor.UntypedAbstractActor;
import com.alibaba.fastjson.JSONObject;
import org.chl.db.data.entity.User;
import org.chl.logic.user.manager.UserMgr;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Auther: chenlin
 * @Date: 2020/1/3
 * @Description: TODO
 */
public class LoginActor extends UntypedAbstractActor {

//    @Autowired
//    private UserMgr userMgr;

    @Override
    public void onReceive(Object message) throws Throwable {
        if(message instanceof String){
            String msg = (String) message;
//            userMgr.loginByHttp(JSONObject.parseObject(msg, User.class));
        }
    }
}
