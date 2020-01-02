package org.chl.logic.user.bo;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import lombok.Getter;
import lombok.Setter;
import org.chl.db.data.entity.User;

/**
 * 玩家
 * @author monster
 *
 */
public class UserBo extends User {
    public static final AttributeKey<UserBo> PLAYER_KEY = AttributeKey.valueOf("player");
    // 玩家通道会话
    public transient final Channel channel;
    // 玩家ip
    public transient String ip;

    public UserBo(Channel channel, String ip) {
        this.channel = channel;
        this.ip = ip;
    }

    @Getter
    @Setter
    private User user;
}
