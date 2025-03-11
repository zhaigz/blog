package com.zgz.controller;

import com.zgz.pojo.Type;
import com.zgz.service.BlogService;
import com.zgz.service.TypeService;
import com.zgz.vo.BlogQuery;
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
 * @ClassName: TypeShowController
 * @Description: TODO
 * @Author: Zgz
 * @Date: 2021/9/3 20:21
 * @Version: 1.0
 **/
@Controller
public class TypeShowController {

    @Autowired
    private TypeService typeService;
    @Autowired
    private BlogService blogService;

    //根据点击的类型进行查询
    @GetMapping("/types/{id}")
    public String types(@PathVariable Long id, @PageableDefault(size = 8,sort = {"id"},direction = Sort.Direction.DESC) Pageable pageable, Model model){
        //拿到博客的所有分类
        List<Type> types = typeService.listTypeTop(10000);
        //id==-1说明是从首页第一次跳转，并没有指明分类
        if(id==-1){
            id=types.get(0).getId();
        }
        BlogQuery blogQuery = new BlogQuery();
        blogQuery.setTypeId(id);
        model.addAttribute("types",types);
        //根据分类的Id查询博客
        model.addAttribute("page",blogService.listBlog(pageable,blogQuery));
        //将类型id返回前端展示
        model.addAttribute("activeTypeId",id);
        return "types";
    }
}
