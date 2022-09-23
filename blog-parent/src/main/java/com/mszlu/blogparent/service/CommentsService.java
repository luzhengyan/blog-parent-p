package com.mszlu.blogparent.service;

import com.mszlu.blogparent.vo.Result;
import com.mszlu.blogparent.vo.params.CommentParam;

public interface CommentsService {
    /**
     * 根据文章id查询所有的评论
     * @param id
     * @return
     */
    Result commentsByArticleId(Long id);

    Result comment(CommentParam commentParam);
}
