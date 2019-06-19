package com.laidw.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 负责一些基本功能的Controller
 */
@Controller
public class BaseController {
    /**
     * 负责返回首页
     * @return index.html页面
     */
    @GetMapping({"/", "/index"})
    public String toIndexPage(){
        return "base/index";
    }

    /**
     * 负责返回登录页面
     * @return login.html页面
     */
    @GetMapping("/login")
    public String toLoginPage(){
        return "base/login";
    }
}
