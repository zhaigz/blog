package com.zgz.controller.admin;

import com.zgz.pojo.Type;
import com.zgz.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * @ClassName: TypeController
 * @Description: TODO
 * @Author: Zgz
 * @Date: 2021/8/31 19:48
 * @Version: 1.0
 **/
@Controller
@RequestMapping("/admin")
public class TypeController {

    @Autowired
    private TypeService typeService;

    @GetMapping("/types")
    public String types(@PageableDefault(size = 10,sort = {"id"},direction = Sort.Direction.DESC) Pageable pageable, Model model){
        model.addAttribute("page",typeService.listType(pageable));
        return "admin/types";
    }

    //这里的Model和model.addAttribute("type",new Type());目的是防止接收页面th:object="${type}"为空
    @GetMapping("/types/inputPage")
    public String inputPage(Model model){
        model.addAttribute("type",new Type());
        return "/admin/types-input";
    }

    //编辑分类，先查找到对应id的分类信息
    @GetMapping("/types/{id}/inputPage")
    public String editInput(@PathVariable Long id, Model model){
        model.addAttribute("type",typeService.getTypeById(id));
        return "admin/types-input";
    }

    //这里@Valid用于标识该变量需要进行校验，BindingResult 存放校验的结果，实体类Type有注解，types-input接收
    //@Valid Type type, BindingResult result挨着，否则没有校验效果
    @PostMapping("/types")
    public String post(@Valid Type type, BindingResult result, RedirectAttributes attributes){
        Type typeByName = typeService.getTypeByName(type.getName());
        if (typeByName!=null){
            //参数0必须和Type实体类中NotBlank对应的属性一致，其余两个自定义
            result.rejectValue("name","nameError","不能添加重复分类");
        }
        if(result.hasErrors()){
            return "admin/types-input";
        }
        Type t = typeService.saveType(type);
        if (t == null){
            attributes.addFlashAttribute("message","新增失败");
        }else {
            attributes.addFlashAttribute("message","新增成功");
        }
        return "redirect:/admin/types";
    }

    //也可以与上面的方法实现共用
    @PostMapping("/types/{id}")
    public String editPost(@Valid Type type, BindingResult result,@PathVariable Long id,RedirectAttributes attributes){
        Type typeByName = typeService.getTypeByName(type.getName());
        if (typeByName!=null){
            //参数0必须和Type实体类中NotBlank对应的属性一致，其余两个自定义
            result.rejectValue("name","nameError","不能添加重复分类");
        }
        if(result.hasErrors()){
            return "admin/types-input";
        }
        Type t = typeService.updateType(id,type);
        if (t == null){
            attributes.addFlashAttribute("message","更新失败");
        }else {
            attributes.addFlashAttribute("message","更新成功");
        }
        return "redirect:/admin/types";

    }

    @GetMapping("/types/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes){
        typeService.deleteType(id);
        attributes.addFlashAttribute("message","删除成功");
        return "redirect:/admin/types";
    }


}
