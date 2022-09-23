package com.mszlu.blogparent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mszlu.blogparent.dao.mapper.CommentsMapper;
import com.mszlu.blogparent.dao.pojo.Comment;
import com.mszlu.blogparent.dao.pojo.SysUser;
import com.mszlu.blogparent.service.CommentsService;
import com.mszlu.blogparent.service.SysUserService;
import com.mszlu.blogparent.utils.UserThreadLocal;
import com.mszlu.blogparent.vo.CommentVo;
import com.mszlu.blogparent.vo.Result;
import com.mszlu.blogparent.vo.UserVo;
import com.mszlu.blogparent.vo.params.CommentParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentsServiceImpl implements CommentsService {
    @Autowired
    private CommentsMapper commentsMapper;
    @Autowired
    private SysUserService sysUserService;
    @Override
    public Result commentsByArticleId(Long id) {
        /**
         * 1. 根据文章id 查询文章列表 从comment表中查询
         * 2. 根据comment表中的作者id 查询作者的信息
         * 3. 判断是否有子评论(level = 1 就是有子评论，即level=1的是父评论)
         * 4. 如果有，根据id 进行查询(parent_id)
         */
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("article_id", id);
        queryWrapper.eq("level", 1);
        List<Comment> commentList = commentsMapper.selectList(queryWrapper);
        List<CommentVo> commentVoList = copyList(commentList);
        return Result.success(commentVoList);
    }

    @Override
    public Result comment(CommentParam commentParam) {
        SysUser sysUser = UserThreadLocal.get();
        Comment comment = new Comment();
        comment.setArticleId(commentParam.getArticleId());
        comment.setAuthorId(sysUser.getId());
        comment.setContent(commentParam.getContent());
        comment.setCreateDate(System.currentTimeMillis());
        Long parent = commentParam.getParent();
        if (parent==null||parent==0){
            comment.setLevel(1);
        }else {
            comment.setLevel(2);
        }
        comment.setParentId(parent==null?0:parent);
        Long toUserId = commentParam.getToUserId();
        comment.setToUid(toUserId==null?0:toUserId);
        this.commentsMapper.insert(comment);
        return Result.success(null);
    }

    private List<CommentVo> copyList(List<Comment> comments) {
        List<CommentVo> commentVoList=new ArrayList<>();
        for(Comment comment : comments){
            commentVoList.add(copy(comment));
        }
        return commentVoList;
    }

    private CommentVo copy(Comment comment) {
        //comment中的数据并不全，需要从其他表进行查询
        CommentVo commentVo = new CommentVo();
        BeanUtils.copyProperties(comment,commentVo);
        //从user表中查询作者信息
        Long authorId = comment.getAuthorId();
        UserVo userVo = sysUserService.findUserVoById(authorId);
        commentVo.setAuthor(userVo);
        //子评论
        Integer level = comment.getLevel();
        if(level==1){
            Long id = comment.getId();//这是父评论的id，根据父评论的id去找子评论
            List<CommentVo> commentVoList = findCommentsByParentsId(id);
            commentVo.setChildrens(commentVoList);
        }
        // to user 给谁评论
        if(level >1){
            Long toUid = comment.getToUid();
            UserVo toUserVo = sysUserService.findUserVoById(toUid);
            commentVo.setToUser(toUserVo);

        }
        return commentVo;
    }

    private List<CommentVo> findCommentsByParentsId(Long id) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id",id);
        queryWrapper.eq("level",2);
        return copyList(commentsMapper.selectList(queryWrapper));
    }
}
