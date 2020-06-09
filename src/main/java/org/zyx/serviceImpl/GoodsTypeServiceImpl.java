package org.zyx.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.zyx.VO.AllTypeVO;
import org.zyx.entity.GoodsType;
import org.zyx.enums.TypeEnum;
import org.zyx.mapper.GoodsTypeMapper;
import org.zyx.service.GoodsTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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
public class GoodsTypeServiceImpl extends ServiceImpl<GoodsTypeMapper, GoodsType> implements GoodsTypeService {

    @Autowired
    private GoodsTypeMapper goodsTypeMapper;

    @Override
    public List<AllTypeVO> getAllType() {
        List<AllTypeVO> allTypeVOList = new ArrayList<>();
        QueryWrapper<GoodsType> wrapper = new QueryWrapper<>();
        wrapper.eq("super_id", TypeEnum.SuperType.getType());
        List<GoodsType> superList = goodsTypeMapper.selectList(wrapper);
        for(GoodsType goodsType : superList){
            AllTypeVO allTypeVO = new AllTypeVO();
            allTypeVO.setSuperType(goodsType);//设置顶级分类
            //根据顶级分类的 typeId查询子分类
            QueryWrapper<GoodsType> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("super_id", goodsType.getTypeId());
            List<GoodsType> childTypeList = goodsTypeMapper.selectList(queryWrapper);
            allTypeVO.setChildTypeList(childTypeList);

            allTypeVOList.add(allTypeVO);
        }
        return allTypeVOList;
    }
}
