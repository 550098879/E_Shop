package org.zyx.VO;

import lombok.Data;
import org.zyx.entity.GoodCar;

/**
 * 购物车所需信息
 */
@Data
public class CartVO{

    private GoodCar goodCar;
    private GoodsVO goodsVO;
}
