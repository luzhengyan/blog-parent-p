package com.mszlu.blogparent.utils;

import com.mszlu.blogparent.dao.pojo.SysUser;

public class UserThreadLocal {
    private UserThreadLocal(){};

    private static final ThreadLocal<SysUser> local=new ThreadLocal<>();


    public static void put(SysUser sysUser){
        local.set(sysUser);
    }
    public static SysUser get(){
        return local.get();
    }
    public static void remove(){
        local.remove();
    }
}
