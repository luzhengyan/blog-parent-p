package com.mszlu.blogparent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mszlu.blogparent.dao.mapper.SysUserMapper;
import com.mszlu.blogparent.dao.pojo.SysUser;
import com.mszlu.blogparent.service.LoginService;
import com.mszlu.blogparent.service.SysUserService;
import com.mszlu.blogparent.vo.LoginUserVo;
import com.mszlu.blogparent.vo.Result;
import com.mszlu.blogparent.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service

public class SysUserServiceImpl implements SysUserService {
    @Override
    public SysUser findUserByAccount(String account) {
        /*LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount,account);
        queryWrapper.last("limit 1");
        return this.sysUserMapper.selectOne(queryWrapper);*/
        return sysUserMapper.findUserByAccount(account);
    }

    @Override
    public UserVo findUserVoById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null){
            sysUser = new SysUser();
            sysUser.setId(1L);
            sysUser.setAvatar("/static/img/logo.b3a48c0.png");
            sysUser.setNickname("默认用户");
        }
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(sysUser,userVo);
        return userVo;
    }


    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired(required = false)
    private LoginService loginService;
    @Override
    public SysUser findUser(String account, String passsword) {
        LambdaQueryWrapper<SysUser> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount,account);
        queryWrapper.eq(SysUser::getPassword,passsword);
        queryWrapper.select(SysUser::getId,SysUser::getAvatar,SysUser::getNickname,SysUser::getAccount);
        queryWrapper.last("limit 1");
        return sysUserMapper.selectOne(queryWrapper);
    }

    @Override
    public Result findUserByToken(String token) {
        /**
         * 1.token合法性校验
         *  是否为空 解析是否成功 Redis是否存在
         * 2.如果校验失败，返回错误
         * 3.如果成功，返回对应结果loginUserVo
         */

        SysUser sysUser=loginService.checkToken(token);
        if (sysUser==null){
            Result.fail(ErrorCode.TOKEN_ERROR.getCode(), ErrorCode.TOKEN_ERROR.getMsg());
        }
        LoginUserVo loginUserVo = new LoginUserVo();
        loginUserVo.setId(sysUser.getId());
        loginUserVo.setNickName(sysUser.getNickname());
        loginUserVo.setAvatar(sysUser.getAvatar());
        loginUserVo.setAccount(sysUser.getAccount());
        return Result.success(loginUserVo);
    }

    @Override
    public void save(SysUser sysUser) {
        this.sysUserMapper.insertAll(sysUser);
    }

    @Override
    public SysUser findUserById(Long id) {
        //根据id查询
        //为防止sysUser为空增加一个判断
        SysUser sysUser = sysUserMapper.findUserById(id);
        if (sysUser == null){
            sysUser = new SysUser();
            sysUser.setNickname("默认用户");
        }
        return sysUser;
    }
}
