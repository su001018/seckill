package com.example.seckill.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
public class TestController {
    @RequestMapping("/hello")
    public String sayHello(Model m){
        m.addAttribute("name","seckill");
        return "hello";
    }
}
