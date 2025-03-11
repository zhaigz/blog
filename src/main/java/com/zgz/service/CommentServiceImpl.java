package com.zgz.service;

import com.zgz.dao.CommentRepository;
import com.zgz.pojo.Comment;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: CommentServiceImpl
 * @Description: TODO
 * @Author: Zgz
 * @Date: 2021/9/9 16:20
 * @Version: 1.0
 **/
@Service
public class CommentServiceImpl implements CommentService{

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<Comment> listCommentByBlogId(Long blogId) {
        //
        Sort sort = Sort.by(Sort.Direction.ASC,"createTime");
        //拿到所有评论
        List<Comment> comments = commentRepository.findByBlogIdAndParentCommentNull(blogId,sort);
        //只拿到顶级的评论 特点：父id为空
        return eachComment(comments);
    }

    @Transactional
    @Override
    //父级id不为空找到父级并设置进来，否则为普通评论
    public Comment saveComment(Comment comment) {
        //拿到父级评论id
        Long parentCommentId = comment.getParentComment().getId();
        //parentCommentId设置的默认值是-1
        if(parentCommentId !=-1){
            //将父级评论设置到子评论中
            comment.setParentComment(commentRepository.findById(parentCommentId).get());
        }else {
            comment.setParentComment(null);
        }
        comment.setCreateTime(new Date());
        return commentRepository.save(comment);
    }

    /*
     *
     * @param null
     * @Description:循环每个顶点的评论节点
     * @Return:
     **/

    private List<Comment> eachComment(List<Comment> comments){
        //新生成一个集合
        List<Comment> commentsView=new ArrayList<>();
        for (Comment comment : comments){
            Comment c = new Comment();
            BeanUtils.copyProperties(comment,c);
            commentsView.add(c);
        }
        // 合并评论的各层子代到第一级子代集合中
        combineChildren(commentsView);
        return commentsView;
    }

    /*
     *
     * @param null
     * @Description:root根节点，blog不为空的对象集合
     * @Return:
     **/
    private void combineChildren(List<Comment> comments){
        for (Comment comment : comments) {
            //拿到子评论
            List<Comment> replys1= comment.getReplyComments();
            for (Comment reply1 : replys1){
                //再循环迭代，找出子代，存放在tempReplys中
                recursively(reply1);
            }
            //修改顶级节点reply集合为迭代处理后的集合，也就是将tempReplys容器和第一级子评论平级设置为父评论中的子评论
            comment.setReplyComments(tempReplys);
            //清除临时存放区
            tempReplys = new ArrayList<>();
        }
    }
    //存放迭代找出的所有子代的集合
    private List<Comment> tempReplys= new ArrayList<>();

    /*
     *
     * @param comment 被迭代的对象
     * @Description:递归迭代，剥洋葱
     * @Return:
     **/
    private void recursively(Comment comment) {
        tempReplys.add(comment);//顶节点添加到临时存放集合
        if (comment.getReplyComments().size()>0){
            List<Comment> replys = comment.getReplyComments();
            for (Comment reply : replys) {
                tempReplys.add(reply);
                if(reply.getReplyComments().size()>0){
                    recursively(reply);
                }
            }
        }
    }
}
