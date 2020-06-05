package org.zyx.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GoodsTypeMapperTest {

    @Autowired
    private GoodsTypeMapper goodsTypeMapper;

    @Test
    public void findAllTest(){
        goodsTypeMapper.findAll(1,5).forEach(System.out::println);
    }

    @Test
    public void test(){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("super_id",0);
        goodsTypeMapper.selectList(wrapper).forEach(System.out::println);
    }
}