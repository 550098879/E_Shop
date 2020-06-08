package org.zyx.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    public DataVO<Users> findAll(int page,int limit){
        DataVO<Users> dataVO = new DataVO();
        dataVO.setCode(0); //返回的code为0,成功
        dataVO.setCount(usersMapper.selectCount(null));
        dataVO.setMsg("管理员数据");
        //分页查询
        Page<Users> page1 =  new Page<>(page, limit);
        dataVO.setData(usersMapper.selectPage(page1,null).getRecords());
        return dataVO;
    }

    @PostMapping("/addUsers")
    public void addUser(Users users){
        usersMapper.insert(users);
    }

    @GetMapping("/deleteById")
    public void deleteById(int userId){
        usersMapper.deleteById(userId);
    }

    @GetMapping("/updateById")
    public void updateById(Users users){
        usersMapper.updateById(users);
    }

    @GetMapping("/findBy")
    public DataVO findByIdOrLoginName(Users users){
        DataVO dataVO = new DataVO();
        dataVO.setCode(0); //返回的code为0,成功
        dataVO.setCount(usersMapper.selectCount(null));
        dataVO.setMsg("查询数据");
        QueryWrapper wrapper = new QueryWrapper();
        if(users.getUserId() != null){
            wrapper.eq("userId",users.getUserId());
        }
        if(users.getLoginName() != null){
            wrapper.like("login_name",users.getLoginName());
        }
        dataVO.setData(usersMapper.selectList(wrapper));
        return dataVO;
    }

}
