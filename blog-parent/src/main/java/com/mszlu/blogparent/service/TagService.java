package com.mszlu.blogparent.service;

import com.mszlu.blogparent.vo.Result;
import com.mszlu.blogparent.vo.TagVo;

import java.util.List;

public interface TagService {
      Result hots(int limit);

    /**
     * 根据文章id查询标签列表
     * @param articleId
     * @return
     */
    List<TagVo> findTagsByArticleId(Long articleId);

    Result findAll();

    Result findAllDetail();

    Result findDetailById(Long id);
}
