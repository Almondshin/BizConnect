package com.bizconnect.adapter.in.web;


import com.bizconnect.application.domain.enums.EnumAgency;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageMoveController {
    @RequestMapping("/")
    public String hello(Model model){
        EnumAgency[] enumAgencies = EnumAgency.values();
        model.addAttribute("enumAgencies" ,enumAgencies);
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
