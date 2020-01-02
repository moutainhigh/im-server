package org.chl.login.service;

import com.alibaba.fastjson.JSONObject;
import org.chl.login.vo.RegisterVo;
import org.chl.login.vo.SigninVo;

/**
 * @Auther: monster
 * @Date: 2019/12/6
 * @Description: TODO
 */
public interface LoginService {
    JSONObject register(RegisterVo model);

    JSONObject signin(SigninVo model);

    JSONObject signout();
}
