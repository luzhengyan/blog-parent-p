package com.mszlu.blogparent.dao.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
//@TableName("ms_sys_user")
public class SysUser {
        //@TableId(type = IdType.ASSIGN_ID) // 默认id类型//雪花算法
    // 以后 用户多了之后，要进行分表操作，id就需要用分布式id了
   //数据库自增
    private Long id;

    private String account;
    @TableField("`admin`")
    private Integer admin;

    private String avatar;

    private Long createDate;

    private Integer deleted;

    private String email;

    private Long lastLogin;

    private String mobilePhoneNumber;

    private String nickname;

    private String password;

    private String salt;

    private String status;
}

