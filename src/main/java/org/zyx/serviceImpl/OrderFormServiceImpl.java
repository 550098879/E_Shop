package org.zyx.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.zyx.VO.GoodsVO;
import org.zyx.VO.OrderDetailsVO;
import org.zyx.entity.OrderDetail;
import org.zyx.entity.OrderForm;
import org.zyx.mapper.GoodsMapper;
import org.zyx.mapper.OrderDetailMapper;
import org.zyx.mapper.OrderFormMapper;
import org.zyx.service.GoodsService;
import org.zyx.service.OrderFormService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author 刈剑丶
 * @since 2020-06-03
 */
@Service
public class OrderFormServiceImpl extends ServiceImpl<OrderFormMapper, OrderForm> implements OrderFormService {

    @Autowired
    private OrderFormMapper orderFormMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private GoodsMapper goodsMapper;



    @Override
    public OrderDetailsVO getOrderDetailsList(int orderId) {

        OrderDetailsVO orderDetailsVO = new OrderDetailsVO();
        OrderForm orderForm = orderFormMapper.selectById(orderId);
        List<OrderDetail> details = orderDetailMapper.selectList(new QueryWrapper<OrderDetail>().eq("order_id", orderId));
        List goodsVOList = new ArrayList();
        for(OrderDetail detail : details){
            GoodsVO goodsVO = goodsService.findById(detail.getGoodId());
            goodsVO.setNum(detail.getNum());
            goodsVOList.add(goodsVO);
        }
        orderDetailsVO.setGoodsVOList(goodsVOList);
        orderDetailsVO.setOrderForm(orderForm);

        return orderDetailsVO;
    }
}
