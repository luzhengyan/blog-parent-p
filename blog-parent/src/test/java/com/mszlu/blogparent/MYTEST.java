package com.mszlu.blogparent;

import com.mszlu.blogparent.dao.pojo.SysUser;
import com.mszlu.blogparent.service.SysUserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MYTEST {
    @Autowired
    private SysUserService sysUserService;

    @Test
    public void test(){
        SysUser sysUser = new SysUser();

        sysUser.setAdmin(1);
        sysUser.setCreateDate(System.currentTimeMillis());
        sysUser.setLastLogin(System.currentTimeMillis());
        sysUser.setAvatar("/static/img/logo.b3a48c0.png");
        sysUser.setDeleted(0);
        sysUser.setSalt("");
        sysUser.setStatus("");
        sysUser.setEmail("");
        sysUserService.save(sysUser);
    }
}
