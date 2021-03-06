package org.zyx.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zyx.VO.GoodsVO;
import org.zyx.entity.Goods;
import org.zyx.enums.GoodPicType;
import org.zyx.enums.GoodsStatus;
import org.zyx.mapper.GoodsMapper;
import org.zyx.mapper.GoodsPicMapper;
import org.zyx.mapper.GoodsTypeMapper;
import org.zyx.service.GoodsService;
import org.zyx.utils.ConnectTencentCloud;

import java.util.ArrayList;
import java.util.Arrays;
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
    @Autowired
    private GoodsPicMapper goodsPicMapper;

    @Autowired
    private ConnectTencentCloud cloud;

    @Override
    public List<GoodsVO> findAllGoods(int page,int limit) {

        List<Goods> goodsList = goodsMapper.selectPage(new Page<>(page, limit), null).getRecords();

        return getGoodsVOList(goodsList);
    }

    @Override
    public List<GoodsVO> findDogFoods() {
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type_id",30);
        List<Goods> list = goodsMapper.selectList(queryWrapper);
        List<Goods> goodsList = null;
        if(list.size() > 4){
            goodsList = list.subList(0,4);
        }else{
            goodsList = list;
        }
        return getGoodsVOList(goodsList);
    }

    @Override
    public List<GoodsVO> findCatFoods() {
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type_id",31);
        List<Goods> list = goodsMapper.selectList(queryWrapper);
        List<Goods> goodsList = null;
        if(list.size() > 4){
            goodsList = list.subList(0,4);
        }else{
            goodsList = list;
        }
        return getGoodsVOList(goodsList);
    }

    @Override
    public List<GoodsVO> findByTypeId(int type_id,int page, int limit) {

        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type_id",type_id);
        List<Goods> goodsList = goodsMapper.selectPage(new Page<>(page, limit), queryWrapper).getRecords();

        return getGoodsVOList(goodsList);
    }

    @Override
    public GoodsVO findById(int goodId) {
        Goods good = goodsMapper.selectById(goodId);
        return getGoodsVOList(Arrays.asList(good)).get(0);
    }

    @Override
    public List<GoodsVO> findByGoodList(List<Goods> goodsList) {

        return getGoodsVOList(goodsList);
    }

    /**
     * 将商品列表转化为GoodVO
     * @param goodsList 商品list
     * @return GoodVoList
     */
    public  List<GoodsVO> getGoodsVOList(List<Goods> goodsList){
        List<GoodsVO> goodsVOList = new ArrayList<>();
        for(Goods good : goodsList){
            GoodsVO goodsVO = new GoodsVO();
            goodsVO.setGoods(good);
            String typeName = goodsTypeMapper.selectById(good.getTypeId()).getTypeName();
            String superName = goodsTypeMapper.selectById(good.getSuperId()).getTypeName();
            goodsVO.setSuperName(superName);
            goodsVO.setTypeName(typeName);
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("pic_type", GoodPicType.cover.getType());//查找封面图
            wrapper.eq("goods_id",good.getGoodId());
            //返回真实的url
            String picPath = goodsPicMapper.selectOne(wrapper).getPicPath();
            goodsVO.setReqCover(picPath);
            String key = picPath.substring(picPath.lastIndexOf('/'));
            goodsVO.setCover(cloud.getUrl(key));
            goodsVOList.add(goodsVO);
        }
        return goodsVOList;
    }


}
