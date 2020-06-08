package org.zyx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.zyx.entity.Users;
import org.zyx.service.UsersService;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class PageSkipHandler {

    @GetMapping("/")
    public String index(){
        return "index";
    }
    @GetMapping("/about")
    public String about(){
        return "about";
    }
    @GetMapping("/buytoday")
    public String buytoday(){
        return "buytoday";
    }

    @GetMapping("/commodity")
    public String commodity(){
        return "commodity";
    }

    @GetMapping("/details")
    public String details(){
        return "details";
    }

    @GetMapping("/information")
    public String information(){
        return "information";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/shopcart")
    public String shopcart(){
        return "shopcart";
    }

    @GetMapping("/admin")
    public String admin(HttpSession session){
        Users user = (Users) session.getAttribute("admin");
        if(user == null){
            return "adminlogin";
        }
        return "admin";
    }

}
