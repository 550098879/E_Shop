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
import org.zyx.mapper.GoodsMapper;
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
    @Autowired
    private GoodsMapper goodsMapper;

    //添加购物车
    @GetMapping("/addCart")
    public int addCart(int goodId, int count, HttpSession session, HttpServletResponse response){
        Buyer buyer = (Buyer) session.getAttribute("buyer");//获取当前登入用户
        if(buyer == null){//若用户为空,则添加cookie
            return ShopCarStatus.COOKIE_ADD.getType();
        }else{
            Integer buyerId = buyer.getBuyerId();

            QueryWrapper<GoodCar> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("goods_id",goodId);
            queryWrapper.eq("buyer_id",buyerId);
            GoodCar one = goodCarMapper.selectOne(queryWrapper);
            if (one != null){ //判断订单是否已存在
                one.setNum(one.getNum()+count);
                goodCarMapper.updateById(one);
                //更新商品数量
                goodsMapper.updateStock(goodId,count);
            }else{
                GoodCar goodCar = new GoodCar();
                goodCar.setBuyerId(buyerId);
                goodCar.setGoodsId(goodId);
                goodCar.setNum(count);
                goodCar.setAddTime(LocalDateTime.now());
                if(goodCarMapper.insert(goodCar) == 0){
                    return ShopCarStatus.CAR_ADD_FAILED.getType();
                }
                goodsMapper.updateStock(goodId,count);//更新数量
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
        int buyerId = buyer.getBuyerId();

        DataVO<CartVO> cartDataVO = new DataVO();
        cartDataVO.setCode(0);
        cartDataVO.setMsg("购物车信息");
        cartDataVO.setCount(goodCarMapper.selectCount(new QueryWrapper<GoodCar>().eq("buyer_id",buyerId)));
        cartDataVO.setData(goodCarService.findAllCartInfo(buyerId));

        return cartDataVO;
    }

    @GetMapping("/getShopCartCount")
    public int getShopCartCount(HttpSession session){
        Buyer buyer = (Buyer) session.getAttribute("buyer");
        return goodCarMapper.selectCount(new QueryWrapper<GoodCar>().eq("buyer_id",buyer.getBuyerId()))  ;
    }

    @GetMapping("/deleteById")
    public Boolean deleteById(int carId){
        goodCarMapper.deleteById(carId);
        return true;
    }



}
