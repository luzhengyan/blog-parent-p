package com.mszlu.blogparent.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.mszlu.blogparent.dao.pojo.SysUser;
import com.mszlu.blogparent.service.LoginService;
import com.mszlu.blogparent.service.SysUserService;
import com.mszlu.blogparent.utils.JWTUtils;
import com.mszlu.blogparent.vo.Result;
import com.mszlu.blogparent.vo.params.LoginParams;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.TimeUnit;
@Service
@Transactional
public class LoginServiceImpl implements LoginService {
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final String slat = "mszlu!@#";

    /**
     * 检查参数是否合法
     * 检查账户名和密码是否在数据库中
     *
     * @param loginParams
     * @return
     */
    @Override
    public Result login(LoginParams loginParams) {
        String account = loginParams.getAccount();
        String password = loginParams.getPassword();
        if (StringUtils.isBlank(account) || StringUtils.isBlank(password)) {
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }

        //加密
        password = DigestUtils.md5Hex(password + slat);

        SysUser sysUser = sysUserService.findUser(account, password);

        if (sysUser == null) {
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(), ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }

        String token = JWTUtils.createToken(sysUser.getId());
        //添加Token到Redis中，设置过期时间为一天
        redisTemplate.opsForValue().set("Token_" + token, JSON.toJSONString(sysUser), 1, TimeUnit.DAYS);

        return Result.success(token);
    }

    /**
     * 检查token是否为空
     * 检查是否可以通过JWT检查
     * 检查token是否在Redis中
     *
     * @param token
     * @return
     */
    @Override
    public SysUser checkToken(String token) {

        if (StringUtils.isBlank(token)) {
            return null;
        }

        Map<String, Object> map = JWTUtils.checkToken(token);
        if (map == null) {
            return null;
        }

        String val = redisTemplate.opsForValue().get("Token_" + token);
        if (StringUtils.isBlank(val)) {
            return null;
        }

        SysUser sysUser = JSON.parseObject(val, SysUser.class);

        return sysUser;
    }

    @Override
    public Result logout(String token) {
        redisTemplate.delete("Token_" + token);
        return Result.success(null);
    }

    /**
     * 检查参数合法
     * 检查是否已经注册
     * 添加到事务中，出现错误进行回滚（注册，生成Token，存入Redis）
     *
     * @param loginParams
     * @return
     */
    @Override
    @Transactional
    public Result register(LoginParams loginParams) {

        if (StringUtils.isBlank(loginParams.getAccount()) ||
                StringUtils.isBlank(loginParams.getPassword()) ||
                StringUtils.isBlank(loginParams.getNickname())) {
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }
        //System.out.println(loginParams);
        SysUser sysUser = sysUserService.findUserByAccount(loginParams.getAccount());
        if (sysUser != null) {
            return Result.fail(ErrorCode.ACCOUNT_EXIST.getCode(), ErrorCode.ACCOUNT_EXIST.getMsg());
        }
        sysUser = new SysUser();
        sysUser.setAccount(loginParams.getAccount());
        sysUser.setNickname(loginParams.getNickname());
        sysUser.setPassword(DigestUtils.md5Hex(loginParams.getPassword() + slat));
        //sysUser.setAdmin(1);
        sysUser.setCreateDate(System.currentTimeMillis());
        sysUser.setLastLogin(System.currentTimeMillis());
        sysUser.setAvatar("/static/img/logo.b3a48c0.png");
        sysUser.setDeleted(0);
        sysUser.setSalt("");
        sysUser.setStatus("");
        sysUser.setEmail("");
        sysUserService.save(sysUser);

        String token = JWTUtils.createToken(sysUser.getId());
        //添加Token到Redis中，设置过期时间为一天
        redisTemplate.opsForValue().set("Token_" + token, JSON.toJSONString(sysUser), 1, TimeUnit.DAYS);

        return Result.success(token);
    }

}
