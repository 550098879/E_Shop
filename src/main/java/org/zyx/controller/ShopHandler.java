package org.zyx.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.zyx.VO.CartVO;
import org.zyx.VO.DataVO;
import org.zyx.entity.*;
import org.zyx.enums.AddressStatus;
import org.zyx.enums.ShopCarStatus;
import org.zyx.mapper.*;
import org.zyx.service.GoodCarService;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

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
    @Autowired
    private BuyerMapper buyerMapper;
    @Autowired
    private OrderFormMapper orderFormMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private AddressMapper addressMapper;

    //添加购物车
    @GetMapping("/addCart")
    public int addCart(int goodId, int count, HttpSession session, HttpServletResponse response) {
        Buyer buyer = (Buyer) session.getAttribute("buyer");//获取当前登入用户
        if (buyer == null) {//若用户为空,则添加cookie
            return ShopCarStatus.COOKIE_ADD.getType();
        } else {
            Integer buyerId = buyer.getBuyerId();

            QueryWrapper<GoodCar> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("goods_id", goodId);
            queryWrapper.eq("buyer_id", buyerId);
            GoodCar one = goodCarMapper.selectOne(queryWrapper);
            if (one != null) { //判断订单是否已存在
                one.setNum(one.getNum() + count);
                goodCarMapper.updateById(one);
            } else {
                GoodCar goodCar = new GoodCar();
                goodCar.setBuyerId(buyerId);
                goodCar.setGoodsId(goodId);
                goodCar.setNum(count);
                goodCar.setAddTime(LocalDateTime.now());
                if (goodCarMapper.insert(goodCar) == 0) {
                    return ShopCarStatus.CAR_ADD_FAILED.getType();
                }
            }
            return ShopCarStatus.CAR_ADD_SUCCESS.getType();
        }

    }

    //直接购物
    @GetMapping("/shop")
    public String shop(HttpSession session) {
        Buyer buyer = (Buyer) session.getAttribute("buyer");//获取当前登入用户
        if (buyer == null) {
            return "login";
        }
        //处理购物逻辑
        //




        return "";
    }

    //获取购物车信息
    @RequestMapping("/getShopCartInfo")
    public DataVO<CartVO> getShopCartInfo(HttpSession session) {
        Buyer buyer = (Buyer) session.getAttribute("buyer");
        int buyerId = buyer.getBuyerId();

        DataVO<CartVO> cartDataVO = new DataVO();
        cartDataVO.setCode(0);
        cartDataVO.setMsg("购物车信息");
        cartDataVO.setCount(goodCarMapper.selectCount(new QueryWrapper<GoodCar>().eq("buyer_id", buyerId)));
        cartDataVO.setData(goodCarService.findAllCartInfo(buyerId));

        return cartDataVO;
    }

    @GetMapping("/getShopCartCount")
    public int getShopCartCount(HttpSession session) {
        Buyer buyer = (Buyer) session.getAttribute("buyer");
        return goodCarMapper.selectCount(new QueryWrapper<GoodCar>().eq("buyer_id", buyer.getBuyerId()));
    }

    @GetMapping("/deleteById")
    public Boolean deleteById(int carId) {
        goodCarMapper.deleteById(carId);
        return true;
    }





    //订单结算
    @Transactional
    @PostMapping("/clearing")
    public boolean clearing(@RequestBody HashMap carIdList, HttpSession session) {
       // carIdList.forEach((k, v) -> System.out.println("key : " + k + " value : " + v));
        Buyer buyer = (Buyer) session.getAttribute("buyer");
        BigDecimal totalMoney = new BigDecimal(0);

        Set set = carIdList.keySet();
        Iterator iterator = set.iterator();
        while(iterator.hasNext()){
            int carId = Integer.parseInt((String) iterator.next());//获取购物车id
            GoodCar goodItem = goodCarMapper.selectById(carId);//获取该项订单数据
            Goods good = goodsMapper.selectById(goodItem.getGoodsId());//获取商品信息
            good.setStock(good.getStock()-goodItem.getNum());
            totalMoney = totalMoney.add(good.getPrice().multiply(new BigDecimal(goodItem.getNum())));//计算总金额
            goodsMapper.updateById(good);//更新商品信数量
            deleteById(carId);//移除购物车对应信息
        }
        //减少用户的余额
        buyer.setBalance(buyer.getBalance().subtract(totalMoney));
        buyerMapper.updateBalance(buyer.getBuyerId(),buyer.getBalance());//更新余额
        session.setAttribute("buyer",buyer);//更新session中的buyer信息

        //将该订单添加到订单表中,并生成对应的订单详情
        OrderForm orderItem = new OrderForm();
        orderItem.setCreateTime(LocalDateTime.now());

        QueryWrapper<Address> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("buyer_id",buyer.getBuyerId());
        queryWrapper.eq("status", AddressStatus.START_USE.getType());
        Address address = addressMapper.selectOne(queryWrapper);
        orderItem.setAddress(address.getAddress());//设置地址



        return true;
    }


}
