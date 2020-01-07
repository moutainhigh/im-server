package org.chl.logic.user.manager;

import lombok.extern.slf4j.Slf4j;
import org.chl.db.data.entity.User;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther: chenlin
 * @Date: 2020/1/3
 * @Description: TODO
 */
@Slf4j
@Component
public class UserMgr {
    // 已经HTTP登录的用户
    private final ConcurrentHashMap<String, User> useres = new ConcurrentHashMap<>();

    public void loginByHttp(User user){
      useres.put(user.getLoginKey(),user);
      log.info("[{}][{}][{}][{}]已经登录HTTP服务器",user.getId(),user.getMailbox(),user.getNickname(),user.getLoginKey());
    }
}
