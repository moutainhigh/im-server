package org.chl.login.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.chl.common.constant.RemoteActorName;
import org.chl.common.email.service.EmailService;
import org.chl.common.model.PairModel;
import org.chl.common.util.Md5Util;
import org.chl.common.util.RandomKeyGenerator;
import org.chl.common.util.ResultUtil;
import org.chl.db.data.entity.User;
import org.chl.db.data.mapper.UserMapper;
import org.chl.login.akka.actor.ActorFactory;
import org.chl.login.dto.SigninDto;
import org.chl.login.service.LoginService;
import org.chl.login.service.UserService;
import org.chl.login.vo.RegisterVo;
import org.chl.login.vo.SigninVo;
import org.springframework.data.convert.EntityWriter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @Auther: monster
 * @Date: 2019/12/6
 * @Description: TODO
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Resource
    private RedisTemplate<String, PairModel<?, ?>> redisTemplate;

    private final UserMapper userMapper;
    private final EmailService emailService;
    private final UserService userService;
    private final HttpServletRequest request;
    private final ActorFactory actorFactory;

    public LoginServiceImpl(UserMapper userMapper, EmailService emailService, UserService userService, HttpServletRequest request, ActorFactory actorFactory) {
        this.userMapper = userMapper;
        this.emailService = emailService;
        this.userService = userService;
        this.request = request;
        this.actorFactory = actorFactory;
    }

    @Override
    public JSONObject register(RegisterVo registerVo) {
        QueryWrapper<User> query = new QueryWrapper<>();
        query.lambda().eq(User::getMailbox,registerVo.getMailbox());
        userMapper.selectCount(query);
        if(userMapper.selectCount(query) > 0){
            return ResultUtil.failure("该邮箱已被占用");
        }
        if(emailService.validCode(registerVo.getMailbox(),registerVo.getCode()) == false){
            return ResultUtil.failure("邮箱验证码错误");
        }
        User user = new User();
        user.setNickname(registerVo.getNickname());
        user.setPassword(Md5Util.encode(registerVo.getPassword()));
        user.setMailbox(registerVo.getMailbox());
        user.setCreateTime(new Date());
        userMapper.insert(user);
        return ResultUtil.success(null,"注册成功");
    }

    @Override
    public JSONObject signin(SigninVo signinVo) {
        QueryWrapper<User> query = new QueryWrapper<>();
        query.eq("mailbox",signinVo.getMailbox());
        query.eq("password",Md5Util.encode(signinVo.getPassword()));
        User user = userMapper.selectOne(query);
        if(user == null){
            return ResultUtil.failure("邮箱或密码错误");
        }
        String token = userService.login(user.getId());
        SigninDto signinDto = new SigninDto(user.getNickname(),user.getMailbox(),token);
        user.setLoginTime(new Date());
        userMapper.updateById(user);
        user.setLoginKey(RandomKeyGenerator.generate(32));
        actorFactory.sendMsg(JSONObject.toJSONString(user), RemoteActorName.tcploginActor);
        return ResultUtil.success((JSONObject) JSONObject.toJSON(signinDto),"登入成功");
    }

    @Override
    public JSONObject signout() {
        userService.logout();
        return ResultUtil.success(null,"登出成功");
    }
}
