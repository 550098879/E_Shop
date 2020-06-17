package org.zyx.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zyx.VO.CartVO;
import org.zyx.VO.DataVO;
import org.zyx.entity.Buyer;
import org.zyx.entity.GoodCar;
import org.zyx.enums.ShopCarStatus;
import org.zyx.mapper.GoodCarMapper;
import org.zyx.service.GoodCarService;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

/**
 * 购物相关
 */

@RequestMapping("/shop")
@RestController
public class ShopHandler {


    @Autowired
    private GoodCarMapper goodCarMapper;
    @Autowired
    private GoodCarService goodCarService;


    //添加购物车
    @GetMapping("/addCart")
    public int addCart(int goodId, int count, HttpSession session, HttpServletResponse response){
        Buyer buyer = (Buyer) session.getAttribute("buyer");//获取当前登入用户
        if(buyer == null){//若用户为空,则添加cookie
            return ShopCarStatus.COOKIE_ADD.getType();
        }else{
            GoodCar goodCar = new GoodCar();
            goodCar.setBuyerId(buyer.getBuyerId());
            goodCar.setGoodsId(goodId);
            goodCar.setNum(count);
            goodCar.setAddTime(LocalDateTime.now());

            if(goodCarMapper.insert(goodCar) == 0){
                return ShopCarStatus.CAR_ADD_FAILED.getType();
            }
            return ShopCarStatus.CAR_ADD_SUCCESS.getType();
        }

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

    //获取购物车信息
    @RequestMapping("/getShopCartInfo")
    public DataVO<CartVO> getShopCartInfo(HttpSession session){
        Buyer buyer = (Buyer) session.getAttribute("buyer");
        if (buyer == null){
            return null;
        }
        int buyerId = buyer.getBuyerId();

        DataVO<CartVO> cartDataVO = new DataVO();
        cartDataVO.setCode(0);
        cartDataVO.setMsg("购物车信息");
        cartDataVO.setCount(goodCarMapper.selectCount(new QueryWrapper<GoodCar>().eq("buyer_id",buyerId)));
        cartDataVO.setData(goodCarService.findAllCartInfo(buyerId));
        System.out.println(cartDataVO);

        return cartDataVO;
    }





}
