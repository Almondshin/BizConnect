package com.bizconnect.adapter.in.web;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageMoveController {
    @RequestMapping("/")
    public String hello(Model model){
        return "index";
    }

    @RequestMapping("/requestPage")
    public String requestPage(Model model){
        return "request";
    }

    @RequestMapping("/registryPage")
    public String registryPage(Model model){
        return "registry";
    }
}
