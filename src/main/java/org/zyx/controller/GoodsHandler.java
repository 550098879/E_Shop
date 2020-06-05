package org.zyx.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zyx.VO.DataVO;
import org.zyx.VO.GoodsVO;
import org.zyx.VO.TypeVO;
import org.zyx.entity.GoodsType;
import org.zyx.mapper.GoodsTypeMapper;
import org.zyx.service.GoodsService;

import java.util.List;

@RestController
@RequestMapping("/goods")
public class GoodsHandler {

    @Autowired
    private GoodsTypeMapper goodsTypeMapper;
    @Autowired
    private GoodsService goodsService;

    @GetMapping("/findAllType")
    public DataVO<TypeVO> findAllType(int page, int limit){
        DataVO<TypeVO> dataVO = new DataVO();
        dataVO.setCode(0); //返回的code为0,成功
        dataVO.setCount(goodsTypeMapper.selectCount(null));
        dataVO.setMsg("商品类型数据");
        //分页查询
        dataVO.setData(goodsTypeMapper.findAll((page-1)*limit,limit));
        return dataVO;
    }

    @GetMapping("/findAllGood")
    public DataVO<GoodsVO> findAllGood(int page, int limit){
        DataVO<GoodsVO> dataVO = new DataVO();
        dataVO.setCode(0); //返回的code为0,成功
        dataVO.setCount(goodsService.count());
        dataVO.setMsg("商品类型数据");
        //分页查询
        List<GoodsVO> allGoods = goodsService.findAllGoods(page, limit);
        dataVO.setData(allGoods);
        return dataVO;
    }

    @GetMapping("/findSuper")
    public List<GoodsType> findSuper(){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("super_id",0);
        return goodsTypeMapper.selectList(wrapper);
    }

    @GetMapping("/findChildType")
    public List<GoodsType> findChildType(int superId){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("super_id",superId);
        return goodsTypeMapper.selectList(wrapper);
    }


}
