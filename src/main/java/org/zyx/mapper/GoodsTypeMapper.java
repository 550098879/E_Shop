package org.zyx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.zyx.VO.TypeVO;
import org.zyx.entity.GoodsType;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 刈剑丶
 * @since 2020-06-03
 */
@Repository
public interface GoodsTypeMapper extends BaseMapper<GoodsType> {

    @Select("select g1.type_id,g1.type_name,u.login_name userName,g2.type_name superName" +
            "    from goods_type g1, goods_type g2 ,users u " +
            "        where u.userId=g1.userId and u.userId=g2.userId " +
            "            and g1.super_id= g2.type_id limit #{start},#{limit}")
    List<TypeVO> findAll(@Param("start") int start,@Param("limit") int limit);

}
