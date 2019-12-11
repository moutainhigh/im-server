package org.chl.login.service.impl;

import org.chl.common.config.RedisConfig;
import org.chl.common.model.PairModel;
import org.chl.common.util.RandomKeyGenerator;
import org.chl.login.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Auther: monster
 * @Date: 2019/12/6
 * @Description: TODO
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private RedisTemplate<String, PairModel<?, ?>> redisTemplate;
    @Autowired
    private HttpServletRequest request;

    @Override
    public String login(Long id) {
        String token = RandomKeyGenerator.generate(32);
        redisTemplate.opsForHash().put(RedisConfig.REDIS_KEY_TOKEN, token, id);
        return token;
    }

    @Override
    public void logout() {
        String token = request.getHeader("x-access-token");
        redisTemplate.opsForHash().delete(RedisConfig.REDIS_KEY_TOKEN, token);
    }
}
