package org.zyx.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import org.zyx.entity.Buyer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.math.BigDecimal;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 刈剑丶
 * @since 2020-06-03
 */
@Repository
public interface BuyerMapper extends BaseMapper<Buyer> {

}
