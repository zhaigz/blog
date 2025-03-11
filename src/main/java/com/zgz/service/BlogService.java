package com.zgz.service;

import com.zgz.pojo.Blog;
import com.zgz.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface BlogService {

    Blog getBlogById(Long id);

    //博客详情获取博客，Markdown转HTML
    Blog getAndConvert(Long id);

    //后端博客管理
    Page<Blog> listBlog(Pageable pageable, BlogQuery blog);

    //前端博客展示
    Page<Blog> listBlog(Pageable pageable);

    //后端查询，根据id查询博客列表
    Page<Blog> listBlog(Long tagId, Pageable pageable);

    //首页全局搜索
    Page<Blog> listBlog(String query,Pageable pageable);

    //前端：根据更新时间展示推荐博客
    List<Blog> listRecommendBlogTop(Integer size);

    //博客归档
    Map<String,List<Blog>> archiveBlog();

    //归档页面：返回博客数目
    Long countBlog();

    Blog saveBlog(Blog blog);

    Blog updateBlog(Long id,Blog blog);

    void deleteBlog(Long id);
}
