package org.zyx.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zyx.VO.DataVO;
import org.zyx.VO.OrderDetailsVO;
import org.zyx.entity.Buyer;
import org.zyx.entity.OrderForm;
import org.zyx.enums.OrderStatus;
import org.zyx.mapper.OrderFormMapper;
import org.zyx.service.OrderFormService;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderHandler {

    @Autowired
    private OrderFormMapper orderFormMapper;
    @Autowired
    private OrderFormService orderFormService;

    @GetMapping("/findOrderForm")
    public DataVO<OrderForm> findOrderForm(int page, int limit,Integer status, HttpSession session){
        DataVO<OrderForm> dataVO = new DataVO();
        Buyer buyer = (Buyer) session.getAttribute("buyer");

        dataVO.setCode(0);
        dataVO.setMsg("订单历史");
        QueryWrapper<OrderForm> queryWrapper = new QueryWrapper<OrderForm>().eq("buyer_id", buyer.getBuyerId());
        queryWrapper.orderByDesc("create_time");
        if(status != null){
            queryWrapper.eq("status",status);
        }
        dataVO.setCount(orderFormMapper.selectCount(queryWrapper));

        List<OrderForm> orderForms = orderFormMapper.selectPage(new Page<>(page, limit), queryWrapper).getRecords();
        dataVO.setData(orderForms);

        return dataVO;
    }

    @GetMapping("/findOrderFormByAdmin")
    public DataVO<OrderForm> findOrderFormByStatus(int page, int limit,Integer status, HttpSession session){
        DataVO<OrderForm> dataVO = new DataVO();

        dataVO.setCode(0);
        dataVO.setMsg("新增订单");
//        QueryWrapper<OrderForm> queryWrapper = new QueryWrapper<OrderForm>().eq("status", OrderStatus.CREATE);
        dataVO.setCount(orderFormMapper.selectCount(null));

        QueryWrapper<OrderForm> queryWrapper = new QueryWrapper<>();
        if(status != null){
            queryWrapper.eq("status",status);
        }
        List<OrderForm> orderForms = orderFormMapper.selectPage(new Page<>(page, limit), queryWrapper).getRecords();
        dataVO.setData(orderForms);

        return dataVO;
    }

    @GetMapping("/changeStatus")
    public boolean changeStatus(int orderId,int status){
        OrderForm orderForm = orderFormMapper.selectById(orderId);

        if(status ==  OrderStatus.SEND_OUT.getType()){//发货
            orderForm.setDeliveryTime(LocalDateTime.now());
            orderForm.setStatus(status);
            if (orderFormMapper.updateById(orderForm) == 0){
                return false;
            }
        }else if(status ==  OrderStatus.COMPLETE.getType()){
            orderForm.setTeceiveTime(LocalDateTime.now());
            orderForm.setStatus(status);
            if (orderFormMapper.updateById(orderForm) == 0){
                return false;
            }
        }
        return true;
    }

    @GetMapping("/deleteById")
    public Boolean deleteById(int orderId){

        if(orderFormMapper.deleteById(orderId) ==0){
            return false;
        }
        return true;
    }

    @GetMapping("/getOrderDetailsInfo")
    public OrderDetailsVO getOrderDetailsInfo(int orderId){
        //使用Service方法获取
        return  orderFormService.getOrderDetailsList(orderId);
    }

}
