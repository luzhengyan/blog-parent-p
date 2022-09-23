package com.mszlu.blogparent.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mszlu.blogparent.dao.pojo.SysUser;
import com.mszlu.blogparent.vo.Result;
import com.mszlu.blogparent.vo.UserVo;

public interface SysUserService  {
    SysUser findUserByAccount(String account);

    UserVo findUserVoById(Long id);

    SysUser findUserById(Long id);

    SysUser findUser(String account, String passsword);

    /**
     *
     * 根据token查询用户信息
     * @param token
     * @return
     */
    Result findUserByToken(String token);


    void save(SysUser sysUser);
}
