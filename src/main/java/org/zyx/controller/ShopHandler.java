package org.zyx.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import org.zyx.VO.CartVO;
import org.zyx.VO.DataVO;
import org.zyx.entity.*;
import org.zyx.enums.AddressStatus;
import org.zyx.enums.ClearingStatus;
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
    @Transactional
    @GetMapping("/shop")
    public int shop(int goodId,int count,HttpSession session) {
        Buyer buyer = (Buyer) session.getAttribute("buyer");//获取当前登入用户
        Goods good = goodsMapper.selectById(goodId);//获取商品信息
        BigDecimal totalMoney = good.getPrice().multiply(new BigDecimal(count));
        if(good.getStock() < count){
            return ClearingStatus.NOT_ENOUGH_STOCK.getType();
        }
        //处理购物逻辑
        //1.生成对应的订单表
        OrderForm orderItem = new OrderForm();
        orderItem.setOrderId((int)System.currentTimeMillis());//使用时间戳作为订单id,改为订单号;优先生成订单号
        orderItem.setCreateTime(LocalDateTime.now());

        //设置订单相关信息
        QueryWrapper<Address> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("buyer_id",buyer.getBuyerId());
        queryWrapper.eq("status", AddressStatus.START_USE.getType());//获取默认地址
        Address address = addressMapper.selectOne(queryWrapper);
        orderItem.setAddress(address.getAddress());//设置地址
        orderItem.setPhone(address.getPhone());
        orderItem.setNicheng(address.getNicheng());//
        orderItem.setBuyerId(buyer.getBuyerId());
        orderItem.setTotal(totalMoney);//订单总价

        //减少用户的余额
        //先判断用户余额是否足够
        if(buyer.getBalance().compareTo(totalMoney) < 0){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ClearingStatus.NOT_ENOUGH_MONEY.getType();
        }
        buyer.setBalance(buyer.getBalance().subtract(totalMoney));
        //更新余额
        Buyer updateBuyer = buyerMapper.selectById(buyer.getBuyerId());
        updateBuyer.setBalance(buyer.getBalance());
        buyerMapper.updateById(updateBuyer);
        session.setAttribute("buyer",buyer);//更新session中的buyer信息

        //更新商品信数量
        good.setStock(good.getStock()-count);
        goodsMapper.updateById(good);

        //2.生成订单详情(以订单id添加)
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setGoodId(good.getGoodId());
        orderDetail.setNum(count);
        orderDetail.setOrderId(orderItem.getOrderId());
        orderDetailMapper.insert(orderDetail);//插入订单详情

        orderFormMapper.insert(orderItem);//添加订单表

        return ClearingStatus.CLEARING_SUCCESS.getType();
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
    public int clearing(@RequestBody HashMap carIdList, HttpSession session) {
       // carIdList.forEach((k, v) -> System.out.println("key : " + k + " value : " + v));
        Buyer buyer = (Buyer) session.getAttribute("buyer");
        BigDecimal totalMoney = new BigDecimal(0);

        //1.生成对应的订单表
        OrderForm orderItem = new OrderForm();
        orderItem.setOrderId((int)System.currentTimeMillis());//使用时间戳作为订单id,改为订单号;优先生成订单号
        orderItem.setCreateTime(LocalDateTime.now());

        //遍历结算购物车项
        Set set = carIdList.keySet();
        Iterator iterator = set.iterator();
        while(iterator.hasNext()){
            int carId = Integer.parseInt((String) iterator.next());//获取购物车id
            GoodCar goodItem = goodCarMapper.selectById(carId);//获取该项订单数据
            Goods good = goodsMapper.selectById(goodItem.getGoodsId());//获取商品信息

            if(good.getStock() < goodItem.getNum()){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//事务回滚
                return ClearingStatus.NOT_ENOUGH_STOCK.getType();
            }

            good.setStock(good.getStock()-goodItem.getNum());
            totalMoney = totalMoney.add(good.getPrice().multiply(new BigDecimal(goodItem.getNum())));//计算总金额
            goodsMapper.updateById(good);//更新商品信数量
            deleteById(carId);//移除购物车对应信息

            //2.循环生成订单详情(以订单id添加)
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setGoodId(good.getGoodId());
            orderDetail.setNum(goodItem.getNum());
            orderDetail.setOrderId(orderItem.getOrderId());

            orderDetailMapper.insert(orderDetail);//插入订单详情

        }
        //减少用户的余额
        //先判断用户余额是否足够
        if(buyer.getBalance().compareTo(totalMoney) < 0){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ClearingStatus.NOT_ENOUGH_MONEY.getType();
        }

        buyer.setBalance(buyer.getBalance().subtract(totalMoney));
        //更新余额
        Buyer updateBuyer = buyerMapper.selectById(buyer.getBuyerId());
        updateBuyer.setBalance(buyer.getBalance());
        buyerMapper.updateById(updateBuyer);

        session.setAttribute("buyer",buyer);//更新session中的buyer信息


        //设置订单相关信息
        QueryWrapper<Address> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("buyer_id",buyer.getBuyerId());
        queryWrapper.eq("status", AddressStatus.START_USE.getType());//获取默认地址
        Address address = addressMapper.selectOne(queryWrapper);
        orderItem.setAddress(address.getAddress());//设置地址
        orderItem.setPhone(address.getPhone());
        orderItem.setNicheng(address.getNicheng());//
        orderItem.setBuyerId(buyer.getBuyerId());
        orderItem.setTotal(totalMoney);//订单总价

        orderFormMapper.insert(orderItem);//添加订单表

        return ClearingStatus.CLEARING_SUCCESS.getType();
    }

}
