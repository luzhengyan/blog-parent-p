package com.mszlu.blogparent.vo.params;


import com.mszlu.blogparent.vo.CategoryVo;
import com.mszlu.blogparent.vo.TagVo;
import lombok.Data;

import java.util.List;

@Data
public class ArticleParam {

    private Long id;

    private ArticleBodyParam body;

    private CategoryVo category;

    private String summary;

    private List<TagVo> tags;

    private String title;
}