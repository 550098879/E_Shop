package org.zyx.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import org.zyx.entity.Address;
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
public interface AddressMapper extends BaseMapper<Address> {

    /**
     * 禁用其他地址
     * @param addressId
     */
    @Update("update address set status = 0 where buyer_id = #{buyerId} and address_id <> #{addressId}")
    void disableOther(@Param("addressId") Integer addressId,@Param("buyerId") Integer buyerId);

    /**
     * 启用该地址作为默认地址
     * @param addressId
     */
    @Update("update address set status = 1 where address_id = #{addressId}")
    void setDefault(@Param("addressId") Integer addressId);

}
