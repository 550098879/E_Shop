package org.zyx.serviceImpl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zyx.VO.GoodsVO;
import org.zyx.entity.Goods;
import org.zyx.mapper.GoodsMapper;
import org.zyx.mapper.GoodsTypeMapper;
import org.zyx.service.GoodsService;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 刈剑丶
 * @since 2020-06-03
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private GoodsTypeMapper goodsTypeMapper;

    @Override
    public List<GoodsVO> findAllGoods(int page,int limit) {

        List<GoodsVO> goodsVOList = new ArrayList<>();
        List<Goods> goodsList = goodsMapper.selectPage(new Page<>(page, limit), null).getRecords();
        for(Goods good : goodsList){
            GoodsVO goodsVO = new GoodsVO();
            goodsVO.setGoods(good);
            String typeName = goodsTypeMapper.selectById(good.getTypeId()).getTypeName();
            String superName = goodsTypeMapper.selectById(good.getSuperId()).getTypeName();
            goodsVO.setSuperName(superName);
            goodsVO.setTypeName(typeName);
            goodsVOList.add(goodsVO);
        }

        return goodsVOList;
    }
}
