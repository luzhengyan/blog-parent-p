package com.mszlu.blogparent.controller;

import com.mszlu.blogparent.common.Cache.Cache;
import com.mszlu.blogparent.common.aop.LogAnnotation;
import com.mszlu.blogparent.service.ArticleService;
import com.mszlu.blogparent.vo.Result;
import com.mszlu.blogparent.vo.params.ArticleParam;
import com.mszlu.blogparent.vo.params.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


//json数据交互
@RestController
@RequestMapping("articles")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    /**
     * 首页文章列表
     * @param pageParams
     * @return
     */
    @PostMapping
    @LogAnnotation(module="文章",operator="获取文章列表")
    @Cache(expire = 5*60*1000,name = "listArticle")
    public Result listArticle(@RequestBody PageParams pageParams){
        //ArticleVo 页面接收的数据

        return articleService.listArticle(pageParams);
    }
    @PostMapping("hot")
    @Cache(expire = 5*60*1000,name = "hot_article")
    public Result hotArticle(){
        int limit = 5;
        return articleService.hotArticle(limit);
    }
    @PostMapping("new")
    @Cache(expire = 5*60*1000,name = "news_article")
    public Result newArticle(){
        int limit = 5;
        return articleService.newArticle(limit);
    }
    @PostMapping("listArchives")
    public Result listArchives(){
        return articleService.listArchives();
    }
    @PostMapping("view/{id}")
    public Result findArticleById(@PathVariable("id") Long articleId){
        return articleService.findArticleById(articleId);
    }
    @PostMapping("publish")
    public Result publish(@RequestBody ArticleParam articleParam){
    return articleService.publish(articleParam);
    }
}
