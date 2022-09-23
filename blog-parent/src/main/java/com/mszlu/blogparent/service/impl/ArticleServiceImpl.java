package com.mszlu.blogparent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mszlu.blogparent.dao.dos.Archives;
import com.mszlu.blogparent.dao.mapper.ArticleBodyMapper;
import com.mszlu.blogparent.dao.mapper.ArticleMapper;
import com.mszlu.blogparent.dao.mapper.ArticleTagMapper;
import com.mszlu.blogparent.dao.pojo.*;
import com.mszlu.blogparent.service.*;
import com.mszlu.blogparent.utils.UserThreadLocal;
import com.mszlu.blogparent.vo.ArticleBodyVo;
import com.mszlu.blogparent.vo.Result;
import com.mszlu.blogparent.vo.ArticleVo;
import com.mszlu.blogparent.vo.TagVo;
import com.mszlu.blogparent.vo.params.ArticleParam;
import com.mszlu.blogparent.vo.params.PageParams;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private TagService tagService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ArticleTagMapper articleTagMapper;

    @Override
    public Result listArticle(PageParams pageParams) {
        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());

        IPage<Article> articleIPage = articleMapper.listArticle(
                page,
                pageParams.getCategoryId(),
                pageParams.getTagId(),
                pageParams.getYear(),
                pageParams.getMonth());
        List<Article> records = articleIPage.getRecords();
        return Result.success(copyList(records, true, true));
    }

    /*@Override
        public Result listArticle(PageParams pageParams) {
            Page<Article> page=new Page<>(pageParams.getPage(), pageParams.getPageSize());
            LambdaQueryWrapper<Article> queryWrapper=new LambdaQueryWrapper<>();
            if (pageParams.getCategoryId()!=null){
                queryWrapper.eq(Article::getCategoryId,pageParams.getCategoryId());
            }
            List<Long> articleIdList=new ArrayList<>();
            if (pageParams.getTagId()!=null){
                QueryWrapper<ArticleTag> articleTagQueryWrapper = new QueryWrapper<>();
                articleTagQueryWrapper.eq("tag_id",pageParams.getTagId());
                List<ArticleTag> articleTags = articleTagMapper.selectList(articleTagQueryWrapper);
                for(ArticleTag articleTag: articleTags){
                    articleIdList.add(articleTag.getArticleId());
                }
                if (articleIdList.size()>0){
                    queryWrapper.in(Article::getId,articleIdList);
                }
            }
            queryWrapper.orderByDesc(Article::getWeight,Article::getCreateDate);
            Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);
            List<Article> records = articlePage.getRecords();
            List<ArticleVo> articleVoList=copyList(records,true,true);

            return Result.success(articleVoList);
        }
    */
    private List<ArticleVo> copyList(List<Article> records,Boolean isTag,Boolean isAuthor) {
        List<ArticleVo> articleVoList=new ArrayList<>();
        for(Article record:records){
            articleVoList.add(copy(record,isTag,isAuthor,false,false));
        }
        return articleVoList;
    }

    private List<ArticleVo> copyList(List<Article> records,Boolean isTag,Boolean isAuthor,Boolean isBody,Boolean isCategory) {
        List<ArticleVo> articleVoList=new ArrayList<>();
        for(Article record:records){
            articleVoList.add(copy(record,isTag,isAuthor,isBody,isCategory));
        }
        return articleVoList;
    }

    @Autowired
    private CategoryService categoryService;
    private ArticleVo copy(Article article,Boolean isTag,Boolean isAuthor,Boolean isBody,Boolean isCategory){
        ArticleVo articleVo=new ArticleVo();
        BeanUtils.copyProperties(article,articleVo);
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        if(isTag){
            Long articleId = article.getId();
            articleVo.setTags(tagService.findTagsByArticleId(articleId));
        }
        if(isAuthor){
            Long authorId = article.getAuthorId();
            articleVo.setAuthor(sysUserService.findUserById(authorId).getNickname());
        }
        if (isBody){
            Long bodyId=article.getBodyId();
            articleVo.setBody(findArticleBodyById(bodyId));
        }
        if (isCategory){
            Long categoryId=article.getCategoryId();
            articleVo.setCategory(categoryService.findCategoryById(categoryId));
        }
        return articleVo;
    }
    @Autowired
    private ArticleBodyMapper articleBodyMapper;
    private ArticleBodyVo findArticleBodyById(Long bodyId) {
        ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }

    @Override
    public Result listArchives() {
        List<Archives> archivesList = articleMapper.listArchives();
        return Result.success(archivesList);
    }

    @Override
    public Result publish(ArticleParam articleParam) {
        SysUser sysUser = UserThreadLocal.get();
        Article article = new Article();
        article.setAuthorId(sysUser.getId());
        article.setWeight(Article.Article_Common);
        article.setViewCounts(0);
        article.setTitle(articleParam.getTitle());
        article.setSummary(articleParam.getSummary());
        article.setCommentCounts(0);
        article.setCreateDate(System.currentTimeMillis());
        article.setCategoryId(articleParam.getCategory().getId());
        this.articleMapper.insert(article);
        List<TagVo> tags = articleParam.getTags();
        //tag
        if (tags!=null){
            for (TagVo tag : tags) {
                Long articleId = article.getId();
                ArticleTag articleTag = new ArticleTag();
                articleTag.setTagId(tag.getId());
                articleTag.setArticleId(articleId);
                articleTagMapper.insert(articleTag);
            }
        }
        //body
        ArticleBody articleBody = new ArticleBody();
        articleBody.setArticleId(article.getId());
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        articleBodyMapper.insert(articleBody);
        article.setBodyId(articleBody.getId());
        articleMapper.updateById(article);
        HashMap<String, String> map = new HashMap<>();
        map.put("id",article.getId().toString());
        return Result.success(map);
    }

    @Override
    public Result findArticleById(Long articleId) {
        Article article=this.articleMapper.selectById(articleId);
        ArticleVo articleVo=copy(article,true,true,true,true);
        threadService.updateArticleViewCount(articleMapper,article);
        return Result.success(articleVo);
    }

    @Override
    public Result newArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getCreateDate);
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.last("limit "+limit);
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articles,false,false));
    }

    @Override
    public Result hotArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getViewCounts);
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.last("limit "+limit);
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articles,false,false));
    }
    @Autowired
    private ThreadService threadService;
}
