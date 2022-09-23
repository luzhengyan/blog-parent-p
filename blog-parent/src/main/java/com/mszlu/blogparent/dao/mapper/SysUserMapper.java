package com.mszlu.blogparent.dao.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mszlu.blogparent.dao.pojo.SysUser;
import org.springframework.stereotype.Repository;

@Repository
public interface SysUserMapper extends BaseMapper<SysUser> {
    SysUser findUserById(Long id);
    //SysUser findUserByAccount(String account);

    //int insertAll(SysUser sysUser);
int insertAll(SysUser sysUser);
    SysUser findUserByAccount(@Param("account") String account);
}
