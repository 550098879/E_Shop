package org.zyx.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zyx.VO.DataVO;
import org.zyx.entity.Buyer;
import org.zyx.entity.Users;
import org.zyx.mapper.BuyerMapper;

@RestController
@RequestMapping("/buyer")
public class BuyerHandler {

    @Autowired
    private BuyerMapper buyerMapper;

    @GetMapping("/findAll")
    public DataVO<Buyer> findAll(int page,int limit){
        DataVO<Buyer> dataVO = new DataVO();
        dataVO.setCode(0); //返回的code为0,成功
        dataVO.setCount(buyerMapper.selectCount(null));
        dataVO.setMsg("买家数据");
        //分页查询
        Page<Buyer> page1 =  new Page<>(page, limit);
        dataVO.setData(buyerMapper.selectPage(page1,null).getRecords());
        return dataVO;
    }

}
