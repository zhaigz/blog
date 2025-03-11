package com.zgz.controller;

import com.zgz.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @ClassName: ArchivesShowController
 * @Description: TODO
 * @Author: Zgz
 * @Date: 2021/9/8 16:22
 * @Version: 1.0
 **/
@Controller
public class ArchivesShowController {
    @Autowired
    private BlogService blogService;

    @GetMapping("/archives")
    public String archives(Model model){
        /*测试输出的年份是否是倒序
        Map<String, List<Blog>> map =new HashMap<>();
        map=blogService.archiveBlog();
        Iterator<String> iterator = map.keySet().iterator();
        while (iterator.hasNext()){
            System.out.println("iterator:"+iterator.next());
        }*/
        model.addAttribute("archiveMap",blogService.archiveBlog());
        //博客列表条数
        model.addAttribute("blogCount",blogService.countBlog());
        return "archives";
    }
}
