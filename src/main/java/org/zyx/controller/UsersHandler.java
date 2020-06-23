package org.zyx.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zyx.VO.DataVO;
import org.zyx.VO.MapVO;
import org.zyx.entity.Users;
import org.zyx.mapper.UsersMapper;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
    
    @PostMapping("/login")
    public MapVO login(Users users, HttpSession session){
        MapVO login = new MapVO();

        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("login_name",users.getLoginName());
        queryWrapper.eq("psw",users.getPsw());
        Users admin = usersMapper.selectOne(queryWrapper);

        System.out.println(admin);

        if(admin != null){
            login.setCode(0);
            login.setMsg("登陆成功");

            //更新管理员状态
            admin.setStatus(1);
            usersMapper.updateById(admin);

            Map map = new HashMap<>();
            map.put("access_token",UUID.randomUUID());
            login.setData(map);
            session.setAttribute("admin",admin);//将admin设置到session中

        }else{
            login.setCode(300);
            login.setMsg("登陆失败");
        }


        return login;
    }

    @GetMapping("/logout")
    public MapVO logout(HttpSession session){
        session.removeAttribute("admin");

        MapVO logout = new MapVO();
        logout.setCode(0);
        logout.setMsg("退出成功");
        logout.setData(null);
        return logout;
    }
    

}
