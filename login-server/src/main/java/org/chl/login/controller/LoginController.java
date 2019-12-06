package org.chl.login.controller;

import com.alibaba.fastjson.JSONObject;
import org.chl.common.util.ResultUtil;
import org.chl.login.model.RegisterModel;
import org.chl.login.model.SigninModel;
import org.chl.login.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @Auther: monster
 * @Date: 2019/12/6
 * @Description: TODO
 */
@RestController
@RequestMapping(value = "/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/register")
    JSONObject register(@Valid RegisterModel model, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return ResultUtil.failure(bindingResult.getFieldError().getDefaultMessage());
        }
        return loginService.register(model);
    }

    @PostMapping("/signin")
    JSONObject signin(@Valid SigninModel model, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return ResultUtil.failure(bindingResult.getFieldError().getDefaultMessage());
        }
        return loginService.signin(model);
    }
}
