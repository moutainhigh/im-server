package org.chl.login.service;

import com.alibaba.fastjson.JSONObject;
import org.chl.login.model.RegisterModel;
import org.chl.login.model.SigninModel;

/**
 * @Auther: monster
 * @Date: 2019/12/6
 * @Description: TODO
 */
public interface LoginService {
    JSONObject register(RegisterModel model);

    JSONObject signin(SigninModel model);

    JSONObject signout();
}
