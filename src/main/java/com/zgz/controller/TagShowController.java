package com.zgz.controller;

import com.zgz.pojo.Tag;
import com.zgz.service.BlogService;
import com.zgz.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @ClassName: tagShowController
 * @Description: TODO
 * @Author: Zgz
 * @Date: 2021/9/3 20:21
 * @Version: 1.0
 **/
@Controller
public class TagShowController {

    @Autowired
    private TagService tagService;
    @Autowired
    private BlogService blogService;

    //根据点击的类型进行查询
    @GetMapping("/tags/{id}")
    public String tags(@PathVariable Long id, @PageableDefault(size = 8,sort = {"id"},direction = Sort.Direction.DESC) Pageable pageable, Model model){
        //拿到博客的所有分类
        List<Tag> tags = tagService.listTagTop(10000);
        //id==-1说明是从首页第一次跳转，并没有指明分类
        if(id==-1){
            id=tags.get(0).getId();
        }
        model.addAttribute("tags",tags);
        //根据标签的Id查询博客列表
        model.addAttribute("page",blogService.listBlog(id,pageable));
        //将类型id返回前端展示
        model.addAttribute("activeTagId",id);
        return "tags";
    }
}
