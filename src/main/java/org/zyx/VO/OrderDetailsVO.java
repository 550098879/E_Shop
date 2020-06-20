package org.zyx.VO;

import lombok.Data;
import org.zyx.entity.OrderDetail;
import org.zyx.entity.OrderForm;

import java.util.List;

/**
 * 商品详情所需信息
 */
@Data
public class OrderDetailsVO {

    private OrderForm orderForm;
    private List<GoodsVO> goodsVOList;

}
