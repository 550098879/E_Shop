package org.zyx.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.zyx.entity.Goods;
import org.zyx.entity.Users;
import org.zyx.mapper.GoodsMapper;
import org.zyx.service.GoodsService;
import org.zyx.service.GoodsTypeService;
import org.zyx.service.UsersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/")
public class PageSkipHandler {

    @Autowired
    private GoodsTypeService goodsTypeService;
    @Autowired
    private GoodsService goodsService;

    @GetMapping("/")
    public ModelAndView index(ModelAndView modelAndView){
        modelAndView.addObject("dogGoods",goodsService.findDogFoods());
        modelAndView.addObject("catGoods",goodsService.findCatFoods());
        modelAndView.addObject("allTypeList",goodsTypeService.getAllType());
        modelAndView.setViewName("index");
        return modelAndView;
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
    public ModelAndView commodity(ModelAndView modelAndView){

        modelAndView.addObject("allTypeList",goodsTypeService.getAllType());
        modelAndView.setViewName("commodity");
        return modelAndView;
    }

    @GetMapping("/details")
    public ModelAndView details(HttpServletRequest request,ModelAndView modelAndView){
        if(request.getAttribute("good") == null){
            return commodity(modelAndView);
        }
        modelAndView.setViewName("details");
        return modelAndView;
    }

    @GetMapping("/information")
    public String information(HttpSession session){
        if(session.getAttribute("buyer") == null){
            return "login";
        }
        return "information";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/reg")
    public String reg(){
        return "reg";
    }


    @GetMapping("/shopcart")
    public String shopCart(){
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
