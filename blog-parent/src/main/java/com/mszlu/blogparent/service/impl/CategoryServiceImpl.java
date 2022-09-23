package com.mszlu.blogparent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mszlu.blogparent.dao.pojo.Category;
import com.mszlu.blogparent.vo.CategoryVo;
import com.mszlu.blogparent.vo.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.mszlu.blogparent.service.CategoryService;
import com.mszlu.blogparent.dao.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author 86139
* @description 针对表【ms_category】的数据库操作Service实现
* @createDate 2022-09-11 08:54:10
*/
@Service
public class CategoryServiceImpl implements CategoryService{
@Autowired
private CategoryMapper categoryMapper;

    @Override
    public CategoryVo findCategoryById(Long categoryId) {
        Category category = categoryMapper.selectById(categoryId);
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category,categoryVo);
        return categoryVo;
    }

    @Override
    public Result findAll() {
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id","category_name");
        List<Category> categoryList = categoryMapper.selectList(queryWrapper);
        return Result.success(copyList(categoryList));
    }

    @Override
    public Result categoryDetailById(Long id) {
        Category category=categoryMapper.selectById(id);
        return Result.success(copy(category));
    }

    @Override
    public Result findAllDetail() {
        List<Category> categoryList = categoryMapper.selectList(null);
        return Result.success(copyList(categoryList));

    }

    private List<CategoryVo> copyList(List<Category> categoryList) {
        List<CategoryVo> list = new ArrayList<>();
        for(Category category:categoryList){
            list.add(copy(category));
        }
        return list;
    }

    private CategoryVo copy(Category category) {
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category,categoryVo);
        return categoryVo;
    }
}




