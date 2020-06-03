package org.zyx.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zyx.VO.DataVO;
import org.zyx.entity.Users;
import org.zyx.mapper.UsersMapper;

@RestController
@RequestMapping("/users")
public class UsersHandler {

    @Autowired
    private UsersMapper usersMapper;

    @GetMapping("/findAll")
    public DataVO findAll(int page,int limit){
        DataVO dataVO = new DataVO();
        dataVO.setCode(0); //返回的code为0,成功
        dataVO.setCount(usersMapper.selectCount(null));
        dataVO.setMsg("管理员数据");
        //分页查询
        Page<Users> page1 =  new Page<>(page, limit);
        Page<Users> usersPage = usersMapper.selectPage(page1, null);
        dataVO.setData(usersMapper.selectPage(page1,null).getRecords());
        return dataVO;
    }



}
