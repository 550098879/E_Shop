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

    /**
     * 查找全部商品,需要分页
     * @param page
     * @param limit
     * @return
     */
    List<GoodsVO> findAllGoods(int page,int limit);

    /**
     * 查找猫粮狗粮,只取前4条
     * @return
     */
    List<GoodsVO> findDogFoods();
    List<GoodsVO> findCatFoods();

    /**
     * 根据类型id查找该类商品
     * @param type_id
     * @param page
     * @param limit
     * @return
     */
    List<GoodsVO> findByTypeId(int type_id,int page,int limit);

    /**
     * 查询单个商品
     * @param goodId
     * @return
     */
    GoodsVO findById(int goodId);


    List<GoodsVO> findByGoodList(List<Goods> goodsList);


}
