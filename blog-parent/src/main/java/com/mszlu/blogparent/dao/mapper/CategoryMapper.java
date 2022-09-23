package com.mszlu.blogparent.dao.mapper;

import com.mszlu.blogparent.dao.pojo.Category;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 86139
* @description 针对表【ms_category】的数据库操作Mapper
* @createDate 2022-09-11 08:54:10
* @Entity pojo.Category
*/
@Repository
public interface CategoryMapper extends BaseMapper<Category> {

}




