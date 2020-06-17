package org.zyx.service;

import org.zyx.VO.CartVO;
import org.zyx.entity.GoodCar;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 购物车 服务类
 * </p>
 *
 * @author 刈剑丶
 * @since 2020-06-03
 */
public interface GoodCarService extends IService<GoodCar> {

    /**
     * 获取前台购物车所需数据
     * @param buyerId
     * @return
     */
    List<CartVO> findAllCartInfo(int buyerId);

}
