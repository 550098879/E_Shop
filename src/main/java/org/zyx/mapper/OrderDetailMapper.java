package org.zyx.mapper;

import org.springframework.stereotype.Repository;
import org.zyx.entity.OrderDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 订单明细表 Mapper 接口
 * </p>
 *
 * @author 刈剑丶
 * @since 2020-06-03
 */
@Repository
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {

}
