package org.chl.login.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.chl.common.email.service.EmailService;
import org.chl.common.util.Md5Util;
import org.chl.common.util.ResultUtil;
import org.chl.db.data.dom.User;
import org.chl.db.data.mapper.UserMapper;
import org.chl.login.model.RegisterModel;
import org.chl.login.model.SigninModel;
import org.chl.login.service.LoginService;
import org.chl.login.service.UserService;
import org.chl.login.view.LoginView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Auther: monster
 * @Date: 2019/12/6
 * @Description: TODO
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserService userService;

    @Override
    public JSONObject register(RegisterModel model) {
        if(userMapper.countByMailbox(model.getMailbox()) > 0){
            return ResultUtil.failure("该邮箱已被占用");
        }
        if(emailService.validCode(model.getMailbox(),model.getCode()) == false){
            return ResultUtil.failure("邮箱验证码错误");
        }
        User user = new User();
        user.setUsername(model.getUsername());
        user.setPassword(Md5Util.encode(model.getPassword()));
        user.setMailbox(model.getMailbox());
        user.setCreatetime(new Date());
        userMapper.insert(user);
        return ResultUtil.success(null,"注册成功");
    }

    @Override
    public JSONObject signin(SigninModel model) {
        User user = userMapper.selectByMailboxAndPassword(model.getMailbox(),Md5Util.encode(model.getPassword()));
        if(user == null){
            return ResultUtil.failure("邮箱或密码错误");
        }
        String token = userService.login(user.getId());
        LoginView view = new LoginView(user.getUsername(),user.getMailbox(),token);
        return ResultUtil.success((JSONObject) JSONObject.toJSON(view),"登入成功");
    }
}