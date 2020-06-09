package org.zyx.service;

import org.zyx.VO.GoodsVO;
import org.zyx.entity.Goods;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 刈剑丶
 * @since 2020-06-03
 */
public interface GoodsService extends IService<Goods> {

    List<GoodsVO> findAllGoods(int page,int limit);
    List<GoodsVO> findDogGoods();
}
