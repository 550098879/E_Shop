package org.zyx.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.zyx.VO.CartVO;
import org.zyx.entity.GoodCar;
import org.zyx.entity.Goods;
import org.zyx.mapper.GoodCarMapper;
import org.zyx.mapper.GoodsMapper;
import org.zyx.service.GoodCarService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.zyx.service.GoodsService;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 购物车 服务实现类
 * </p>
 *
 * @author 刈剑丶
 * @since 2020-06-03
 */
@Service
public class GoodCarServiceImpl extends ServiceImpl<GoodCarMapper, GoodCar> implements GoodCarService {

    @Autowired
    private GoodCarMapper goodCarMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private GoodsService goodsService;

    @Override
    public List<CartVO> findAllCartInfo(int buyerId) {
        List<CartVO> cartVOList = new ArrayList<>();

        //第一步:获取购物车信息
        List<GoodCar> carList = goodCarMapper.selectList(new QueryWrapper<GoodCar>().eq("buyer_id",buyerId));
        //第二步:遍历购物车集合,获取        List<Goods> goodsList = new ArrayList<>();集合
        for (GoodCar goodCar : carList){
            CartVO cartVO = new CartVO();
            cartVO.setGoodCar(goodCar);
            cartVO.setGoodsVO(goodsService.findById(goodCar.getGoodsId()));
            cartVOList.add(cartVO);
        }
        return cartVOList;
    }
}
