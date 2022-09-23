package com.mszlu.blogparent.controller;

import com.mszlu.blogparent.service.LoginService;
import com.mszlu.blogparent.vo.Result;
import com.mszlu.blogparent.vo.params.LoginParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("login")
public class LoginController {

    @Autowired
    LoginService loginService;

    /**
     * 用户登录
     * @param loginParams
     * @return
     */
    @PostMapping
    public Result login(@RequestBody LoginParams loginParams) {
        return loginService.login(loginParams);
    }
}

