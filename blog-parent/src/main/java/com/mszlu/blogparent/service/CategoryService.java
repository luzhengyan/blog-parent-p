package com.mszlu.blogparent.service;

import com.mszlu.blogparent.vo.CategoryVo;
import com.mszlu.blogparent.vo.Result;

/**
* @author 86139
* @description 针对表【ms_category】的数据库操作Service
* @createDate 2022-09-11 08:54:10
*/
public interface CategoryService  {

    CategoryVo findCategoryById(Long categoryId);

    Result findAll();

    Result findAllDetail();

    Result categoryDetailById(Long id);
}
