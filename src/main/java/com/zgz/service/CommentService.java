package com.zgz.service;

import com.zgz.pojo.Comment;

import java.util.List;

public interface CommentService {

    //通过博客id获取评论列表
    List<Comment> listCommentByBlogId(Long blogId);

    //保存评论信息
    Comment saveComment(Comment comment);
}
