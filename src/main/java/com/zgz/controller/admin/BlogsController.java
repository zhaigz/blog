package com.zgz.controller.admin;

import com.zgz.pojo.Blog;
import com.zgz.pojo.User;
import com.zgz.service.BlogService;
import com.zgz.service.TagService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

/**
 * @ClassName: BlogsController
 * @Description: TODO
 * @Author: Zgz
 * @Date: 2021/8/31 18:40
 * @Version: 1.0
 **/
@Controller
@RequestMapping("/admin")
public class BlogsController {

    private static final String INPUTPAGE = "admin/blogs-input";
    private static final String LIST = "admin/blogs";
    private static final String REDIRECT_LIST = "redirect:/admin/blogs";


    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;

    @GetMapping("/blogs")
    public String blogs(@PageableDefault(size = 2,sort = "updateTime",direction = Sort.Direction.DESC) Pageable pageable, BlogQuery blog, Model model){
        //初始化分类
        model.addAttribute("types",typeService.listType());
        model.addAttribute("page",blogService.listBlog(pageable,blog));
        return "admin/blogs";
    }
    //仅用于搜索时返回blog片段，实现局部刷新
    @PostMapping("/blogs/search")
    public String search(@PageableDefault(size = 2,sort = "updateTime",direction = Sort.Direction.DESC) Pageable pageable, BlogQuery blog, Model model){
        model.addAttribute("page",blogService.listBlog(pageable,blog));
        return "admin/blogs :: blogList";
    }

    @GetMapping("/blogs/inputPage")
    public String inputPage(Model model){
        //标签、分类初始化
        setTypeAndTag(model);
        model.addAttribute("blog",new Blog());
        return "admin/blogs-input";
    }

    public void setTypeAndTag(Model model){
        //初始化分类
        model.addAttribute("types",typeService.listType());
        //初始化标签
        model.addAttribute("tags",tagService.listTag());
    }

    @GetMapping("/blogs/{id}/inputPage")
    public String editInputPage(@PathVariable Long id, Model model){
        //标签、分类初始化
       setTypeAndTag(model);
        Blog blog = blogService.getBlogById(id);
        blog.init();
        model.addAttribute("blog",blog);
        return "admin/blogs-input";
    }

    //新增、编辑
    @PostMapping("/blogs")
    public String post(Blog blog, HttpSession session, RedirectAttributes attributes){
        blog.setUser((User) session.getAttribute("user"));
        //根据选择的类型、标签进行对象的初始化
        blog.setType(typeService.getTypeById(blog.getType().getId()));
        blog.setTags(tagService.listTag(blog.getTagIds()));

        Blog b ;
        //防止更新时view 、createTime值为空 因为编辑时没有拿到其值
        if (blog.getId()==null) {
            b=blogService.saveBlog(blog);
        }else {
            b=blogService.updateBlog(blog.getId(),blog);
        }
        if (b == null){
            attributes.addFlashAttribute("message","操作失败");
        }else {
            attributes.addFlashAttribute("message","操作成功");
        }
        return "redirect:/admin/blogs";
    }

    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes){
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message","删除成功");
        return "redirect:/admin/blogs";
    }
}
