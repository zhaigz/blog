package com.zgz.controller;

import com.zgz.pojo.Comment;
import com.zgz.pojo.User;
import com.zgz.service.BlogService;
import com.zgz.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

/**
 * @ClassName: CommentController
 * @Description: TODO
 * @Author: Zgz
 * @Date: 2021/9/9 16:13
 * @Version: 1.0
 **/
@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private BlogService blogService;

    //评论区用户头像
    @Value("${comment.avatar}")
    private String avatar;

    //根据blogId获取评论列表
    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable Long blogId, Model model){
        model.addAttribute("comments",commentService.listCommentByBlogId(blogId));
        return "blog :: commentList";
    }

    @PostMapping("/comments")
    public String post(Comment comment, HttpSession session){
        Long blogId = comment.getBlog().getId();
        //根据blogId获取博客
        comment.setBlog(blogService.getBlogById(blogId));
        //如果管理员登录，设置管理员头像并设置为管理员博客标记
        User user = (User) session.getAttribute("user");
        if(user !=null){
            comment.setAvatar(user.getAvatar());
            comment.setAdminComment(true);
        }else {
            //设置comment对象中的头像
            comment.setAvatar(avatar);
        }
        commentService.saveComment(comment);
        return "redirect:/comments/"+blogId;
    }

}
