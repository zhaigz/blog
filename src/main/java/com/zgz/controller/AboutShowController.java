package com.zgz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @ClassName: AboutShowController
 * @Description: TODO
 * @Author: Zgz
 * @Date: 2021/9/8 19:37
 * @Version: 1.0
 **/
@Controller
public class AboutShowController {

    @GetMapping("/about")
    public String about(){
        return "about";
    }
}
