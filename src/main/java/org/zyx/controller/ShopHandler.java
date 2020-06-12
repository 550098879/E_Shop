package org.zyx.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zyx.entity.Buyer;

import javax.servlet.http.HttpSession;

/**
 * 购物相关
 */

@RequestMapping("/shop")
@RestController
public class ShopHandler {

    //添加购物车
    @GetMapping("/addCart")
    public String addCart(){


        return null;
    }

    //直接购物
    @GetMapping("/shop")
    public String shop(HttpSession session){
        Buyer buyer = (Buyer) session.getAttribute("buyer");//获取当前登入用户
        if(buyer == null){

            return "login";
        }
        //处理购物逻辑




        return "";
    }



}
