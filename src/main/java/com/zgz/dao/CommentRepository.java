package com.zgz.dao;

import com.zgz.pojo.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//long类型主键
public interface CommentRepository extends JpaRepository<Comment,Long> {

    //拿到父评论为空的评论集合
    List<Comment> findByBlogIdAndParentCommentNull(Long blogId, Sort sort);
}
