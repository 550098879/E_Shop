package org.zyx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.zyx.service.UsersService;

@Controller
@RequestMapping("/")
public class indexHandler{

    @Autowired
    private UsersService usersService;

    @GetMapping("/index")
    public ModelAndView index(ModelAndView modelAndView){
        modelAndView.addObject("list",usersService.list());
        modelAndView.setViewName("index");
        return modelAndView;
    }


}
