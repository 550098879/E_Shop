package org.zyx.serviceImpl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zyx.service.GoodsTypeService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GoodsTypeServiceImplTest {

    @Autowired
    private GoodsTypeService goodsTypeService;

    @Test
    public void allTypeTest(){
        goodsTypeService.getAllType().forEach(System.out::println);
    }
}