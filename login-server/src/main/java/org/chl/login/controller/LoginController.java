package org.chl.login.controller;

import com.alibaba.fastjson.JSONObject;
import org.chl.common.util.ResultUtil;
import org.chl.login.service.LoginService;
import org.chl.login.vo.RegisterVo;
import org.chl.login.vo.SigninVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
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
    JSONObject register(@Valid RegisterVo registerVo, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return ResultUtil.failure(bindingResult.getFieldError().getDefaultMessage());
        }
        return loginService.register(registerVo);
    }

    @PostMapping("/signin")
    JSONObject signin(@Valid SigninVo signinVo, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return ResultUtil.failure(bindingResult.getFieldError().getDefaultMessage());
        }
        return loginService.signin(signinVo);
    }

    @GetMapping("/signout")
    JSONObject signout() {
        return loginService.signout();
    }
}
