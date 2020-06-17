package org.zyx.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import org.zyx.entity.Goods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 刈剑丶
 * @since 2020-06-03
 */
@Repository
public interface GoodsMapper extends BaseMapper<Goods> {

    @Update("update goods set stock = stock - #{count} where good_id = #{goodId}")
    void updateStock(@Param("goodId") int goodId,@Param("count") int count);
}
