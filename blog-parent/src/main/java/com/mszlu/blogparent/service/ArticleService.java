package com.mszlu.blogparent.service;

import com.mszlu.blogparent.vo.Result;
import com.mszlu.blogparent.vo.params.ArticleParam;
import com.mszlu.blogparent.vo.params.PageParams;

public interface ArticleService {
    /**
     * 分页查询文章列表
     * @param pageParams
     * @return
     */
    Result listArticle(PageParams pageParams);

    /**
     * 最热文章
     * @param limit
     * @return
     */
    Result hotArticle(int limit);

    /**
     * 最新文章
     * @param limit
     * @return
     */
    Result newArticle(int limit);

    Result listArchives();

    /**
     * 查询文章详情
     * @param articleId
     * @return
     */
    Result findArticleById(Long articleId);

    Result publish(ArticleParam articleParam);
}
