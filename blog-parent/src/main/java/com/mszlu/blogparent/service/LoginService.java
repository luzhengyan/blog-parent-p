package com.mszlu.blogparent.service;

import com.mszlu.blogparent.dao.pojo.SysUser;
import com.mszlu.blogparent.vo.Result;
import com.mszlu.blogparent.vo.params.LoginParams;

public interface LoginService {
    Result login(LoginParams loginParams);

    SysUser checkToken(String token);

    Result logout(String token);

    Result register(LoginParams loginParams);
}
