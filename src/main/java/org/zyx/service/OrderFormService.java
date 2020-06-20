package org.zyx.service;

import org.zyx.VO.OrderDetailsVO;
import org.zyx.entity.OrderForm;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author 刈剑丶
 * @since 2020-06-03
 */
public interface OrderFormService extends IService<OrderForm> {

    OrderDetailsVO getOrderDetailsList(int orderId);
}
